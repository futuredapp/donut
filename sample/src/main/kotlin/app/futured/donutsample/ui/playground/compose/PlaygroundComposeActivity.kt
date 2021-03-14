package app.futured.donutsample.ui.playground.compose

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import app.futured.donut.compose.data.DonutConfig
import app.futured.donut.compose.data.DonutModel
import app.futured.donut.compose.data.DonutSection
import app.futured.donutsample.R
import app.futured.donutsample.tools.view.setupSeekbar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.random.Random.Default.nextFloat

class PlaygroundComposeActivity : AppCompatActivity() {

    private companion object {
        const val DATA_DELAY = 600L
        private const val INITIAL_CAP = 8f
        private const val INITIAL_MASTER_PROGRESS = 0f
        private const val INITIAL_GAP_WIDTH = 90f
        private const val INITIAL_GAP_ANGLE = 270f
        private const val INITIAL_STROKE_WIDTH = 40f
        private const val INITIAL_ANIMATION_DURATION = 1000

        val INITIAL_DONUT_DATA = DonutModel(
            cap = INITIAL_CAP,
            masterProgress = INITIAL_MASTER_PROGRESS,
            gapWidthDegrees = INITIAL_GAP_WIDTH,
            gapAngleDegrees = INITIAL_GAP_ANGLE,
            strokeWidth = INITIAL_STROKE_WIDTH,
            backgroundLineColor = Color(0xFFE7E8E9),
            sections = listOf(
                DonutSection(amount = 0f, color = Color(0xFF222222)),
                DonutSection(amount = 0f, color = Color(0xFF19D3C5)),
                DonutSection(amount = 0f, color = Color(0xFFFF5F00)),
                DonutSection(amount = 0f, color = Color(0xFF005FCC))
            )
        )

        val INITIAL_DONUT_CONFIG = DonutConfig.create(
            layoutAnimationSpec = tween(
                durationMillis = INITIAL_ANIMATION_DURATION,
                easing = CubicBezierEasing(0.18f, 0.7f, 0.16f, 1f)
            ),
            colorAnimationSpec = tween(durationMillis = INITIAL_ANIMATION_DURATION)
        )
    }

    private val data = MutableStateFlow(INITIAL_DONUT_DATA)
    private val config = MutableStateFlow(INITIAL_DONUT_CONFIG)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_playground_compose)

        findViewById<ComposeView>(R.id.donut_view).apply {
            setContent {
                val data by data.collectAsState()
                val config by config.collectAsState()
                SampleComposeScreen(data, config)
            }
        }

        initControls()
        changeDonutDataWithDelay()
    }

    private fun changeDonutDataWithDelay() {
        Handler().postDelayed({
            data.value = data.value.copy(
                masterProgress = 1f,
                sections = listOf(
                    DonutSection(amount = 1f, color = Color(0xFF222222)),
                    DonutSection(amount = 1f, color = Color(0xFF19D3C5)),
                    DonutSection(amount = 1f, color = Color(0xFFFF5F00)),
                    DonutSection(amount = 1f, color = Color(0xFF005FCC))
                )
            )
        }, DATA_DELAY)
    }

    private fun initControls() {
        // Region - seekbars
        setupSeekbar(
            seekBar = findViewById(R.id.master_progress_seekbar),
            titleTextView = findViewById(R.id.master_progress_text),
            initProgress = 100,
            getTitleText = { getString(R.string.master_progress, it) },
            onProgressChanged = { progress ->
                mutateData { data -> data.copy(masterProgress = progress / 100f) }
            }
        )

        setupSeekbar(
            seekBar = findViewById(R.id.gap_width_seekbar),
            titleTextView = findViewById(R.id.gap_width_text),
            initProgress = INITIAL_GAP_WIDTH.toInt(),
            getTitleText = { getString(R.string.gap_width, it) },
            onProgressChanged = { progress ->
                mutateData { data -> data.copy(gapWidthDegrees = progress.toFloat()) }
            }
        )

        setupSeekbar(
            seekBar = findViewById(R.id.gap_angle_seekbar),
            titleTextView = findViewById(R.id.gap_angle_text),
            initProgress = INITIAL_GAP_ANGLE.toInt(),
            getTitleText = { getString(R.string.gap_angle, it) },
            onProgressChanged = { progress ->
                mutateData { data -> data.copy(gapAngleDegrees = progress.toFloat()) }
            }
        )

        setupSeekbar(
            seekBar = findViewById(R.id.stroke_width_seekbar),
            titleTextView = findViewById(R.id.stroke_width_text),
            initProgress = INITIAL_STROKE_WIDTH.toInt(),
            getTitleText = { getString(R.string.stroke_width, it) },
            onProgressChanged = { progress ->
                mutateData { data -> data.copy(strokeWidth = progress.toFloat()) }
            }
        )

        setupSeekbar(
            seekBar = findViewById(R.id.cap_seekbar),
            titleTextView = findViewById(R.id.cap_text),
            initProgress = INITIAL_CAP.toInt(),
            getTitleText = { getString(R.string.amount_cap, it.toFloat()) },
            onProgressChanged = { progress ->
                mutateData { data -> data.copy(cap = progress.toFloat()) }
            }
        )
        //endregion

        //region - data manipulation
        findViewById<View>(R.id.button_add).setOnClickListener {
            mutateData { data ->
                data.copy(sections = data.sections.map { section -> section.copy(amount = section.amount + nextFloat()) })
            }
        }

        findViewById<View>(R.id.button_remove).setOnClickListener {
            mutateData { data ->
                data.copy(sections = data.sections.map { section ->
                    section.copy(
                        amount = (section.amount - nextFloat()).coerceAtLeast(minimumValue = 0f)
                    )
                })
            }
        }

        findViewById<View>(R.id.button_random_values).setOnClickListener {
            mutateData { data ->
                data.copy(sections = data.sections.map { section ->
                    section.copy(amount = nextFloat() + nextFloat())
                })
            }
        }

        findViewById<View>(R.id.button_random_colors).setOnClickListener {
            mutateData { data ->
                data.copy(sections = data.sections.map { section ->
                    section.copy(color = Color(nextFloat(), nextFloat(), nextFloat()))
                })
            }
        }

        findViewById<View>(R.id.button_clear).setOnClickListener {
            mutateData { data ->
                data.copy(sections = data.sections.map { section ->
                    section.copy(amount = 0f)
                })
            }
        }
        //endregion

        // TODO
        //region - animations
//        findViewById<SwitchCompat>(R.id.anim_enabled_switch).apply {
//            isChecked = true
//            setOnCheckedChangeListener { _, isChecked ->
//                config.setLayoutAnimationsEnabled(isChecked)
//            }
//        }
//
//        findViewById<SwitchCompat>(R.id.anim_colors_enabled_switch).apply {
//            isChecked = true
//            setOnCheckedChangeListener { _, isChecked ->
//                config.setColorAnimationsEnabled(isChecked)
//            }
//        }
//
//        setupSeekbar(
//            seekBar = findViewById(R.id.anim_duration_seekbar),
//            titleTextView = findViewById(R.id.anim_duration_text),
//            initProgress = INITIAL_ANIMATION_DURATION,
//            getTitleText = { getString(R.string.animation_duration, it) },
//            onProgressChanged = { updateAnimationBuilders() }
//        )
//
//        findViewById<RadioGroup>(R.id.interpolator_radio_group).setOnCheckedChangeListener { _, _ ->
//            updateAnimationBuilders()
//        }
        //endregion
    }

    // TODO
//    private fun updateAnimationBuilders() {
//        val radioGroup = findViewById<RadioGroup>(R.id.interpolator_radio_group)
//        for (index in 0 until radioGroup.childCount) {
//            if (radioGroup.getChildAt(index).id == radioGroup.checkedRadioButtonId) {
//                val progress = findViewById<SeekBar>(R.id.anim_duration_seekbar).progress
//                updateAnimationBuilders(progress, index)
//                break
//            }
//        }
//    }
//
//    private fun updateAnimationBuilders(progress: Int, index: Int) {
//        val animationBuilders = listOf(
//            PhysicsBuilder<Float>().apply {
//                dampingRatio = Spring.DampingRatioMediumBouncy
//                stiffness = Spring.StiffnessLow
//                displacementThreshold = 0.0001f
//            },
//            TweenBuilder<Float>().apply {
//                easing = FastOutSlowInEasing
//            },
//            TweenBuilder<Float>().apply {
//                easing = LinearOutSlowInEasing
//            },
//            TweenBuilder<Float>().apply {
//                easing = FastOutLinearInEasing
//            },
//            TweenBuilder<Float>().apply {
//                easing = LinearEasing
//            },
//            customAnimationBuilder
//        )
//
//        val builders = animationBuilders.map { builder ->
//            if (builder is TweenBuilder) {
//                builder.duration = progress
//            }
//            builder
//        }
//
//        val builder = builders[index]
//        findViewById<SeekBar>(R.id.anim_duration_seekbar).isEnabled = builder !is PhysicsBuilder
//
//        config.setLayoutAnimationBuilder(builder)
//    }

    private fun mutateData(mapper: (data: DonutModel) -> DonutModel) {
        data.value = mapper(data.value)
    }

    private fun mutateConfig(mapper: (config: DonutConfig) -> DonutConfig) {
        config.value = mapper(config.value)
    }
}
