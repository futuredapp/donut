package app.futured.donutsample.ui.playground

import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import app.futured.donut.DonutDataset
import app.futured.donutsample.R
import app.futured.donutsample.data.model.BlackCategory
import app.futured.donutsample.data.model.DataCategory
import app.futured.donutsample.data.model.GreenCategory
import app.futured.donutsample.data.model.OrangeCategory
import app.futured.donutsample.tools.extensions.*
import kotlinx.android.synthetic.main.activity_playground.*
import kotlin.random.Random

class PlaygroundActivity : AppCompatActivity() {

    companion object {
        private val ALL_CATEGORIES = listOf(
            BlackCategory,
            GreenCategory,
            OrangeCategory
        )
    }

    private val datasets = mutableListOf<DonutDataset>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playground)

        setupDonut()
        updateIndicators()
        initControls()
        Handler().postDelayed({
            fillInitialData()
            runInitialAnimation()
        }, 1500)
    }

    private fun setupDonut() {
        donut_view.cap = 5f
        donut_view.masterProgress = 0f
        donut_view.alpha = 0f
    }

    private fun runInitialAnimation() {
        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 1000
            interpolator = FastOutSlowInInterpolator()
            addUpdateListener {
                donut_view.masterProgress = it.animatedValue as Float
                donut_view.alpha = it.animatedValue as Float

                master_progress_seekbar.progress = (donut_view.masterProgress * 100).toInt()
            }

            start()
        }
    }

    private fun fillInitialData() {
        datasets += DonutDataset(
            BlackCategory.name,
            getColorCompat(BlackCategory.colorRes),
            1f
        )

        datasets += DonutDataset(
            GreenCategory.name,
            getColorCompat(GreenCategory.colorRes),
            1.2f
        )

        datasets += DonutDataset(
            OrangeCategory.name,
            getColorCompat(OrangeCategory.colorRes),
            1.4f
        )

        donut_view.submitData(datasets)

        updateIndicators()
    }

    private fun updateIndicators() {
        amount_cap_text.text = getString(R.string.amount_cap, donut_view.cap)
        amount_total_text.text = getString(
            R.string.amount_total,
            datasets.sumByFloat { it.amount }
        )

        updateIndicatorAmount(BlackCategory, black_dataset_text)
        updateIndicatorAmount(GreenCategory, green_dataset_text)
        updateIndicatorAmount(OrangeCategory, orange_dataset_text)
    }

    private fun updateIndicatorAmount(category: DataCategory, textView: TextView) {
        datasets
            .filter { it.name == category.name }
            .sumByFloat { it.amount }
            .also {
                if (it > 0f) {
                    textView.visible()
                    textView.text = getString(R.string.float_2f, it)
                } else {
                    textView.gone()
                }
            }
    }

    private fun initControls() {

        // region Styles

        setupSeekbar(
            seekBar = master_progress_seekbar,
            titleTextView = master_progress_text,
            initProgress = (donut_view.masterProgress * 100).toInt(),
            getTitleText = { getString(R.string.master_progress, it) },
            onProgressChanged = { donut_view.masterProgress = it / 100f }
        )

        setupSeekbar(
            seekBar = gap_width_seekbar,
            titleTextView = gap_width_text,
            initProgress = donut_view.gapWidthDegrees.toInt(),
            getTitleText = { getString(R.string.gap_width, it) },
            onProgressChanged = { donut_view.gapWidthDegrees = it.toFloat() }
        )

        setupSeekbar(
            seekBar = gap_angle_seekbar,
            titleTextView = gap_angle_text,
            initProgress = donut_view.gapAngleDegrees.toInt(),
            getTitleText = { getString(R.string.gap_angle, it) },
            onProgressChanged = { donut_view.gapAngleDegrees = it.toFloat() }
        )

        setupSeekbar(
            seekBar = stroke_width_seekbar,
            titleTextView = stroke_width_text,
            initProgress = donut_view.strokeWidth.toInt(),
            getTitleText = { getString(R.string.stroke_width, it) },
            onProgressChanged = { donut_view.strokeWidth = it.toFloat() }
        )

        // endregion

        // region Data

        setupSeekbar(
            seekBar = cap_seekbar,
            titleTextView = cap_text,
            initProgress = donut_view.cap.toInt(),
            getTitleText = { getString(R.string.amount_cap, it.toFloat()) },
            onProgressChanged = {
                donut_view.cap = it.toFloat()
                updateIndicators()
            }
        )

        // Add entry with random category and random amount
        button_add.setOnClickListener {
            val randomCategory = ALL_CATEGORIES.random()
            if (datasets.any { it.name == randomCategory.name }.not()) {
                datasets.add(
                    DonutDataset(
                        name = randomCategory.name,
                        color = getColorCompat(randomCategory.colorRes),
                        amount = 0f
                    )
                )
            }

            val randomIndex = datasets.indexOfFirst { it.name == randomCategory.name }
            datasets.modifyAt(randomIndex) {
                it.copy(amount = it.amount + Random.nextFloat())
            }

            donut_view.submitData(datasets)
            updateIndicators()
        }

        // Remove random entry
        button_remove.setOnClickListener {
            if (datasets.isNotEmpty()) {
                val randomIndex = datasets.indices.random()
                datasets.modifyAt(randomIndex) {
                    it.copy(amount = it.amount - Random.nextFloat())
                }
                if (datasets[randomIndex].amount <= 0f) {
                    datasets.removeAt(randomIndex)
                }

                donut_view.submitData(datasets)
                updateIndicators()
            }
        }

        // Randomize data set colors
        button_random_colors.setOnClickListener {
            for (i in 0 until datasets.size) {
                datasets[i] = datasets[i].copy(color = Random.nextInt())
            }

            donut_view.submitData(datasets)
        }

        // Clear graph
        button_clear.setOnClickListener {
            datasets.clear()
            donut_view.clear()
            updateIndicators()
        }

        // endregion

        // region Animations

        anim_enabled_switch.isChecked = donut_view.animationEnabled
        anim_enabled_switch.setOnCheckedChangeListener { _, isChecked ->
            donut_view.animationEnabled = isChecked
        }

        setupSeekbar(
            seekBar = anim_duration_seekbar,
            titleTextView = anim_duration_text,
            initProgress = donut_view.animationDurationMs.toInt(),
            getTitleText = { getString(R.string.animation_duration, it) },
            onProgressChanged = { donut_view.animationDurationMs = it.toLong() }
        )

        val interpolators = listOf(
            AnimationUtils.loadInterpolator(this, android.R.interpolator.decelerate_quint),
            AnimationUtils.loadInterpolator(this, android.R.interpolator.accelerate_quint),
            AnimationUtils.loadInterpolator(this, android.R.interpolator.accelerate_decelerate),
            AnimationUtils.loadInterpolator(this, android.R.interpolator.linear),
            AnimationUtils.loadInterpolator(this, android.R.interpolator.bounce)
        )

        interpolator_radio_group.setOnCheckedChangeListener { _, checkedId ->
            donut_view.animationInterpolator = interpolators[checkedId - 1]
        }

        // endregion
    }

    private fun setupSeekbar(
        seekBar: SeekBar,
        titleTextView: TextView,
        initProgress: Int,
        getTitleText: (progress: Int) -> String,
        onProgressChanged: (progress: Int) -> Unit
    ) {
        titleTextView.text = getTitleText(initProgress)
        seekBar.apply {
            progress = initProgress
            doOnProgressChange {
                onProgressChanged(progress)
                titleTextView.text = getTitleText(progress)
            }
        }
    }
}
