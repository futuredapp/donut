package app.futured.donutsample.ui.playground

import android.animation.ValueAnimator
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import app.futured.donut.DonutDirection
import app.futured.donut.DonutProgressView
import app.futured.donut.DonutSection
import app.futured.donutsample.R
import app.futured.donutsample.data.model.BlackCategory
import app.futured.donutsample.data.model.DataCategory
import app.futured.donutsample.data.model.GreenCategory
import app.futured.donutsample.data.model.OrangeCategory
import app.futured.donutsample.tools.extensions.getColorCompat
import app.futured.donutsample.tools.extensions.gone
import app.futured.donutsample.tools.extensions.sumByFloat
import app.futured.donutsample.tools.extensions.visible
import app.futured.donutsample.tools.view.setupSeekbar
import kotlin.random.Random

class PlaygroundActivity : AppCompatActivity() {

    companion object {
        private val ALL_CATEGORIES = listOf(
            BlackCategory,
            GreenCategory,
            OrangeCategory
        )
    }

    private val donutProgressView by lazy { findViewById<DonutProgressView>(R.id.donut_view) }
    private val masterProgressSeekbar by lazy { findViewById<SeekBar>(R.id.master_progress_seekbar) }
    private val masterProgressText by lazy { findViewById<TextView>(R.id.master_progress_text) }
    private val gapWidthSeekbar by lazy { findViewById<SeekBar>(R.id.gap_width_seekbar) }
    private val gapWidthText by lazy { findViewById<TextView>(R.id.gap_width_text) }
    private val gapAngleSeekbar by lazy { findViewById<SeekBar>(R.id.gap_angle_seekbar) }
    private val gapAngleText by lazy { findViewById<TextView>(R.id.gap_angle_text) }
    private val strokeWidthSeekbar by lazy { findViewById<SeekBar>(R.id.stroke_width_seekbar) }
    private val strokeWidthText by lazy { findViewById<TextView>(R.id.stroke_width_text) }
    private val capSeekbar by lazy { findViewById<SeekBar>(R.id.cap_seekbar) }
    private val capText by lazy { findViewById<TextView>(R.id.cap_text) }
    private val animationDurationSeekbar by lazy { findViewById<SeekBar>(R.id.anim_duration_seekbar) }
    private val animationDurationText by lazy { findViewById<TextView>(R.id.anim_duration_text) }
    private val addButton by lazy { findViewById<TextView>(R.id.button_add) }
    private val removeButton by lazy { findViewById<TextView>(R.id.button_remove) }
    private val randomColorsButton by lazy { findViewById<TextView>(R.id.button_random_colors) }
    private val clearButton by lazy { findViewById<TextView>(R.id.button_clear) }
    private val animationEnabledSwitch by lazy { findViewById<SwitchCompat>(R.id.anim_enabled_switch) }
    private val amountCapText by lazy { findViewById<TextView>(R.id.amount_cap_text) }
    private val amountTotalText by lazy { findViewById<TextView>(R.id.amount_total_text) }
    private val blackSectionText by lazy { findViewById<TextView>(R.id.black_section_text) }
    private val greenSectionText by lazy { findViewById<TextView>(R.id.green_section_text) }
    private val orangeSectionText by lazy { findViewById<TextView>(R.id.orange_section_text) }
    private val interpolatorRadioGroup by lazy { findViewById<RadioGroup>(R.id.interpolator_radio_group) }
    private val directionSwitch by lazy { findViewById<SwitchCompat>(R.id.direction_switch) }
    private val strokeCapRadioGroup by lazy { findViewById<RadioGroup>(R.id.stroke_caps_radio_group) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playground)

        updateIndicators()
        setupDonut()
        initControls()
        Handler().postDelayed({
            fillInitialData()
            runInitialAnimation()
        }, 800)
    }

    private fun setupDonut() {
        donutProgressView.cap = 5f
        donutProgressView.masterProgress = 0f
        donutProgressView.gapAngleDegrees = 0f
    }

    private fun runInitialAnimation() {
        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 1000
            interpolator = FastOutSlowInInterpolator()
            addUpdateListener {
                donutProgressView.masterProgress = it.animatedValue as Float
                donutProgressView.alpha = it.animatedValue as Float

                masterProgressSeekbar.progress = (donutProgressView.masterProgress * 100).toInt()
            }

            start()
        }
    }

    private fun fillInitialData() {
        val sections = listOf(
            DonutSection(
                BlackCategory.name,
                getColorCompat(BlackCategory.colorRes),
                1f
            ),
            DonutSection(
                GreenCategory.name,
                getColorCompat(GreenCategory.colorRes),
                1.2f
            ),
            DonutSection(
                OrangeCategory.name,
                getColorCompat(OrangeCategory.colorRes),
                1.4f
            )
        )

        donutProgressView.submitData(sections)

        updateIndicators()
    }

    private fun updateIndicators() {
        amountCapText.text = getString(R.string.amount_cap, donutProgressView.cap)
        amountTotalText.text = getString(
            R.string.amount_total,
            donutProgressView.getData().sumByFloat { it.amount }
        )

        updateIndicatorAmount(BlackCategory, blackSectionText)
        updateIndicatorAmount(GreenCategory, greenSectionText)
        updateIndicatorAmount(OrangeCategory, orangeSectionText)
    }

    private fun updateIndicatorAmount(category: DataCategory, textView: TextView) {
        donutProgressView.getData()
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
            seekBar = masterProgressSeekbar,
            titleTextView = masterProgressText,
            initProgress = (donutProgressView.masterProgress * 100).toInt(),
            getTitleText = { getString(R.string.master_progress, it) },
            onProgressChanged = { donutProgressView.masterProgress = it / 100f }
        )

        setupSeekbar(
            seekBar = gapWidthSeekbar,
            titleTextView = gapWidthText,
            initProgress = donutProgressView.gapWidthDegrees.toInt(),
            getTitleText = { getString(R.string.gap_width, it) },
            onProgressChanged = { donutProgressView.gapWidthDegrees = it.toFloat() }
        )

        setupSeekbar(
            seekBar = gapAngleSeekbar,
            titleTextView = gapAngleText,
            initProgress = donutProgressView.gapAngleDegrees.toInt(),
            getTitleText = { getString(R.string.gap_angle, it) },
            onProgressChanged = { donutProgressView.gapAngleDegrees = it.toFloat() }
        )

        setupSeekbar(
            seekBar = strokeWidthSeekbar,
            titleTextView = strokeWidthText,
            initProgress = donutProgressView.strokeWidth.toInt(),
            getTitleText = { getString(R.string.stroke_width, it) },
            onProgressChanged = { donutProgressView.strokeWidth = it.toFloat() }
        )

        directionSwitch.isChecked = donutProgressView.direction == DonutDirection.ANTICLOCKWISE
        directionSwitch.setOnCheckedChangeListener { _, isChecked ->
            val newDirection = if (isChecked) {
                DonutDirection.ANTICLOCKWISE
            } else {
                DonutDirection.CLOCKWISE
            }
            donutProgressView.direction = newDirection
            updateDirectionSwitchText()
        }
        updateDirectionSwitchText()

        // endregion

        // region Data

        setupSeekbar(
            seekBar = capSeekbar,
            titleTextView = capText,
            initProgress = donutProgressView.cap.toInt(),
            getTitleText = { getString(R.string.amount_cap, it.toFloat()) },
            onProgressChanged = {
                donutProgressView.cap = it.toFloat()
                updateIndicators()
            }
        )

        // Add random amount to random section
        addButton.setOnClickListener {
            val randomCategory = ALL_CATEGORIES.random()
            donutProgressView.addAmount(
                randomCategory.name,
                Random.nextFloat(),
                getColorCompat(randomCategory.colorRes)
            )

            updateIndicators()
        }

        // Remove random value from random section
        removeButton.setOnClickListener {
            val existingSections = donutProgressView.getData().map { it.name }
            if (existingSections.isNotEmpty()) {
                donutProgressView.removeAmount(existingSections.random(), Random.nextFloat())
                updateIndicators()
            }
        }

        // Randomize data set colors
        randomColorsButton.setOnClickListener {
            val sections = donutProgressView.getData().toMutableList()
            for (i in 0 until sections.size) {
                sections[i] = sections[i].copy(color = Random.nextInt())
            }

            donutProgressView.submitData(sections)
        }

        // Clear graph
        clearButton.setOnClickListener {
            donutProgressView.clear()
            updateIndicators()
        }

        // endregion

        // region Animations

        animationEnabledSwitch.isChecked = donutProgressView.animateChanges
        animationEnabledSwitch.setOnCheckedChangeListener { _, isChecked ->
            donutProgressView.animateChanges = isChecked
        }

        setupSeekbar(
            seekBar = animationDurationSeekbar,
            titleTextView = animationDurationText,
            initProgress = donutProgressView.animationDurationMs.toInt(),
            getTitleText = { getString(R.string.animation_duration, it) },
            onProgressChanged = { donutProgressView.animationDurationMs = it.toLong() }
        )

        val interpolators = listOf(
            AnimationUtils.loadInterpolator(this, android.R.interpolator.decelerate_quint),
            AnimationUtils.loadInterpolator(this, android.R.interpolator.accelerate_quint),
            AnimationUtils.loadInterpolator(this, android.R.interpolator.accelerate_decelerate),
            AnimationUtils.loadInterpolator(this, android.R.interpolator.linear),
            AnimationUtils.loadInterpolator(this, android.R.interpolator.bounce)
        )

        interpolatorRadioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            for (i in 0 until radioGroup.childCount) {
                if (radioGroup.getChildAt(i).id == checkedId) {
                    donutProgressView.animationInterpolator = interpolators[i]
                    break
                }
            }
        }

        strokeCapRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            donutProgressView.strokeCap = when (checkedId) {
                R.id.stroke_cap_round -> Paint.Cap.ROUND
                R.id.stroke_cap_butt -> Paint.Cap.BUTT
                else -> error("Unexpected id: $checkedId")
            }
        }

        // endregion
    }

    private fun updateDirectionSwitchText() {
        val directionString = when (donutProgressView.direction) {
            DonutDirection.CLOCKWISE -> resources.getString(R.string.direction_clockwise)
            DonutDirection.ANTICLOCKWISE -> resources.getString(R.string.direction_anticlockwise)
        }
        directionSwitch.text = resources.getString(R.string.direction, directionString)
    }
}
