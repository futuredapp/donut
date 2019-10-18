package com.thefuntasty.donutsample

import android.os.Bundle
import android.os.Handler
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.thefuntasty.donut.DonutProgressEntry
import kotlinx.android.synthetic.main.activity_sample.*
import kotlin.random.Random

class SampleActivity : AppCompatActivity() {

    companion object {
        private val ALL_CATEGORIES = listOf(
            BlackCategory,
            GreenCategory,
            OrangeCategory
        )
    }

    private val dataItems = mutableListOf<DataItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        setupDonut()
        updateIndicators()
        initControls()
        runDelayed(500) { fillInitialData() }
    }

    private fun setupDonut() {
        donut_view.cap = 5f

        ALL_CATEGORIES.forEach {
            donut_view.setColor(it.name, getColorCompat(it.colorRes))
        }
    }

    private fun fillInitialData() {
        dataItems += DataItem(BlackCategory, 1f)
        dataItems += DataItem(GreenCategory, 1.2f)
        dataItems += DataItem(OrangeCategory, 1.4f)

        donut_view.submitData(dataItems.toDonutEntries())
        updateIndicators()
    }

    private fun updateIndicators() {
        amount_cap_text.text = getString(R.string.amount_cap, donut_view.cap)
        amount_total_text.text = getString(R.string.amount_total, dataItems.sumByDouble { it.amount.toDouble() }.toFloat())

        updateIndicatorAmount(BlackCategory, black_dataset_text)
        updateIndicatorAmount(GreenCategory, green_dataset_text)
        updateIndicatorAmount(OrangeCategory, orange_dataset_text)
    }

    private fun updateIndicatorAmount(category: DataCategory, textView: TextView) {
        dataItems
            .filter { it.category == category }
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
        master_progress_text.text = getString(
            R.string.master_progress,
            (donut_view.masterProgress * 100f).toInt()
        )
        master_progress_seekbar.apply {
            progress = (donut_view.masterProgress * 100f).toInt()
            doOnProgressChange {
                donut_view.masterProgress = it / 100f
                master_progress_text.text = getString(R.string.master_progress, it)
            }
        }

        gap_width_text.text = getString(R.string.gap_width, donut_view.gapWidthDegrees.toInt())
        gap_width_seekbar.apply {
            progress = donut_view.gapWidthDegrees.toInt()
            doOnProgressChange {
                donut_view.gapWidthDegrees = it.toFloat()
                gap_width_text.text = getString(R.string.gap_width, it)
            }
        }

        gap_angle_text.text = getString(R.string.gap_angle, donut_view.gapAngleDegrees.toInt())
        gap_angle_seekbar.apply {
            progress = donut_view.gapAngleDegrees.toInt()
            doOnProgressChange {
                donut_view.gapAngleDegrees = it.toFloat()
                gap_angle_text.text = getString(R.string.gap_angle, it)
            }
        }

        stroke_width_text.text = getString(R.string.stroke_width, donut_view.strokeWidth.toInt())
        stroke_width_seekbar.apply {
            progress = donut_view.strokeWidth.toInt()
            doOnProgressChange {
                donut_view.strokeWidth = it.toFloat()
                stroke_width_text.text = getString(R.string.stroke_width, it)
            }
        }

        // Add random amount to random category
        button_add.setOnClickListener {
            dataItems.add(
                DataItem(
                    ALL_CATEGORIES.random(),
                    Random.nextFloat()
                )
            )

            donut_view.submitData(dataItems.toDonutEntries())
            updateIndicators()
        }

        // Remove random amount from random category
        button_remove.setOnClickListener {
            if (dataItems.isNotEmpty()) {
                val randomIndex = dataItems.indexOf(dataItems.random())
                dataItems.removeAt(randomIndex)
                donut_view.submitData(dataItems.toDonutEntries())
                updateIndicators()
            }
        }

        button_random_colors.setOnClickListener {
            ALL_CATEGORIES.forEach {
                donut_view.setColor(it.name, Random.nextInt())
            }
        }

        // Clear graph
        button_clear.setOnClickListener {
            dataItems.clear()
            donut_view.clear()
            updateIndicators()
        }

        cap_text.text = getString(R.string.amount_cap, donut_view.cap)
        cap_seekbar.apply {
            progress = donut_view.cap.toInt()
            doOnProgressChange {
                donut_view.cap = it.toFloat()
                cap_text.text = getString(R.string.amount_cap, it.toFloat())
                updateIndicators()
            }
        }
    }

    private fun AppCompatActivity.getColorCompat(id: Int) = ContextCompat.getColor(this, id)

    private fun runDelayed(delayMs: Long, r: () -> Unit) =
        Handler().postDelayed(r, delayMs)

    private fun SeekBar.doOnProgressChange(f: (progress: Int) -> Unit) {
        setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    f(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
        })
    }

    private fun List<DataItem>.toDonutEntries() = map { it.toDonutEntry() }

    private fun DataItem.toDonutEntry() = DonutProgressEntry(
        category = category.name,
        amount = amount
    )
}
