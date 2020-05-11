package app.futured.donutsample.ui.playground.compose

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.SeekBar
import androidx.animation.CubicBezierEasing
import androidx.animation.FastOutLinearInEasing
import androidx.animation.FastOutSlowInEasing
import androidx.animation.LinearEasing
import androidx.animation.LinearOutSlowInEasing
import androidx.animation.PhysicsBuilder
import androidx.animation.Spring
import androidx.animation.TweenBuilder
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.ui.core.setContent
import androidx.ui.graphics.Color
import app.futured.donut.library.compose.data.DonutConfig
import app.futured.donut.library.compose.data.DonutData
import app.futured.donut.library.compose.data.DonutDataset
import app.futured.donutsample.R
import app.futured.donutsample.tools.view.setupSeekbar
import kotlin.math.max
import kotlin.random.Random.Default.nextFloat

class PlaygroundComposeActivity : AppCompatActivity() {

    private companion object {
        const val DATA_DELAY = 600L
        const val INITIAL_CAP = 8f
        const val INITIAL_MASTER_PROGRESS = 0f
        const val INITIAL_GAP_WIDTH = 90f
        const val INITIAL_GAP_ANGLE = 270f
        const val INITIAL_STROKE_WIDTH = 40f
        const val INITIAL_ANIMATION_DURATION = 1000
    }

    private val data = DonutData(
        cap = INITIAL_CAP,
        masterProgress = INITIAL_MASTER_PROGRESS,
        gapWidthDegrees = INITIAL_GAP_WIDTH,
        gapAngleDegrees = INITIAL_GAP_ANGLE,
        strokeWidth = INITIAL_STROKE_WIDTH,
        backgroundLineColor = Color.LightGray,
        datasets = listOf(
            DonutDataset(amount = 0f, color = Color(0xFF222222)),
            DonutDataset(amount = 0f, color = Color(0xFF19D3C5)),
            DonutDataset(amount = 0f, color = Color(0xFFFF5F00)),
            DonutDataset(amount = 0f, color = Color(0xFF005FCC))
        )
    )

    private val customAnimationBuilder = TweenBuilder<Float>().apply {
        duration = INITIAL_ANIMATION_DURATION
        easing = CubicBezierEasing(0.18f, 0.7f, 0.16f, 1f)
    }

    private val config = DonutConfig.create(
        layoutAnimationBuilder = customAnimationBuilder
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_playground_compose)

        findViewById<ViewGroup>(R.id.donut_view).setContent {
            SampleComposeScreen(data, config)
        }

        initControls()
        changeDonutDataWithDelay()
    }

    private fun changeDonutDataWithDelay() {
        Handler().postDelayed({
            with(data) {
                masterProgress = 1f
                datasets = listOf(
                    DonutDataset(amount = 1f, color = Color(0xFF222222)),
                    DonutDataset(amount = 1f, color = Color(0xFF19D3C5)),
                    DonutDataset(amount = 1f, color = Color(0xFFFF5F00)),
                    DonutDataset(amount = 1f, color = Color(0xFF005FCC))
                )
            }
        }, DATA_DELAY)
    }

    private fun initControls() {
        // Region - seekbars
        setupSeekbar(
            seekBar = findViewById(R.id.master_progress_seekbar),
            titleTextView = findViewById(R.id.master_progress_text),
            initProgress = 100,
            getTitleText = { getString(R.string.master_progress, it) },
            onProgressChanged = { data.masterProgress = it / 100f }
        )

        setupSeekbar(
            seekBar = findViewById(R.id.gap_width_seekbar),
            titleTextView = findViewById(R.id.gap_width_text),
            initProgress = INITIAL_GAP_WIDTH.toInt(),
            getTitleText = { getString(R.string.gap_width, it) },
            onProgressChanged = { data.gapWidthDegrees = it.toFloat() }
        )

        setupSeekbar(
            seekBar = findViewById(R.id.gap_angle_seekbar),
            titleTextView = findViewById(R.id.gap_angle_text),
            initProgress = INITIAL_GAP_ANGLE.toInt(),
            getTitleText = { getString(R.string.gap_angle, it) },
            onProgressChanged = { data.gapAngleDegrees = it.toFloat() }
        )

        setupSeekbar(
            seekBar = findViewById(R.id.stroke_width_seekbar),
            titleTextView = findViewById(R.id.stroke_width_text),
            initProgress = INITIAL_STROKE_WIDTH.toInt(),
            getTitleText = { getString(R.string.stroke_width, it) },
            onProgressChanged = { data.strokeWidth = it.toFloat() }
        )

        setupSeekbar(
            seekBar = findViewById(R.id.cap_seekbar),
            titleTextView = findViewById(R.id.cap_text),
            initProgress = INITIAL_CAP.toInt(),
            getTitleText = { getString(R.string.amount_cap, it.toFloat()) },
            onProgressChanged = {
                data.cap = it.toFloat()
            }
        )
        //endregion

        //region - data manipulation
        findViewById<View>(R.id.button_add).setOnClickListener {
            data.datasets.random().apply {
                amount += nextFloat()
            }
        }

        findViewById<View>(R.id.button_remove).setOnClickListener {
            for (entry in data.datasets.shuffled()) {
                if (entry.amount != 0f) {
                    entry.amount = max(0f, entry.amount - nextFloat())
                    break
                }
            }
        }

        findViewById<View>(R.id.button_random_values).setOnClickListener {
            data.datasets.forEach {
                it.amount = nextFloat() + nextFloat()
            }
        }

        findViewById<View>(R.id.button_random_colors).setOnClickListener {
            data.datasets.forEach {
                it.color = Color(nextFloat(), nextFloat(), nextFloat())
            }
        }

        findViewById<View>(R.id.button_clear).setOnClickListener {
            data.datasets.forEach {
                it.amount = 0f
            }
        }
        //endregion

        //region - animations
        findViewById<SwitchCompat>(R.id.anim_enabled_switch).apply {
            isChecked = true
            setOnCheckedChangeListener { _, isChecked ->
                config.setLayoutAnimationsEnabled(isChecked)
            }
        }

        findViewById<SwitchCompat>(R.id.anim_colors_enabled_switch).apply {
            isChecked = true
            setOnCheckedChangeListener { _, isChecked ->
                config.setColorAnimationsEnabled(isChecked)
            }
        }

        setupSeekbar(
            seekBar = findViewById(R.id.anim_duration_seekbar),
            titleTextView = findViewById(R.id.anim_duration_text),
            initProgress = INITIAL_ANIMATION_DURATION,
            getTitleText = { getString(R.string.animation_duration, it) },
            onProgressChanged = { updateAnimationBuilders() }
        )

        findViewById<RadioGroup>(R.id.interpolator_radio_group).setOnCheckedChangeListener { _, _ ->
            updateAnimationBuilders()
        }
        //endregion
    }

    private fun updateAnimationBuilders() {
        val radioGroup = findViewById<RadioGroup>(R.id.interpolator_radio_group)
        for (index in 0 until radioGroup.childCount) {
            if (radioGroup.getChildAt(index).id == radioGroup.checkedRadioButtonId) {
                val progress = findViewById<SeekBar>(R.id.anim_duration_seekbar).progress
                updateAnimationBuilders(progress, index)
                break
            }
        }
    }

    private fun updateAnimationBuilders(progress: Int, index: Int) {
        val animationBuilders = listOf(
            PhysicsBuilder<Float>().apply {
                dampingRatio = Spring.DampingRatioMediumBouncy
                stiffness = Spring.StiffnessLow
                displacementThreshold = 0.0001f
            },
            TweenBuilder<Float>().apply {
                easing = FastOutSlowInEasing
            },
            TweenBuilder<Float>().apply {
                easing = LinearOutSlowInEasing
            },
            TweenBuilder<Float>().apply {
                easing = FastOutLinearInEasing
            },
            TweenBuilder<Float>().apply {
                easing = LinearEasing
            },
            customAnimationBuilder
        )

        val builders = animationBuilders.map { builder ->
            if (builder is TweenBuilder) {
                builder.duration = progress
            }
            builder
        }

        val builder = builders[index]
        findViewById<SeekBar>(R.id.anim_duration_seekbar).isEnabled = builder !is PhysicsBuilder

        config.setLayoutAnimationBuilder(builder)
    }
}
