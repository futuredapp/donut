package com.thefuntasty.donutsample

import android.os.Bundle
import android.os.Handler
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.thefuntasty.donut.DonutProgressEntry
import kotlinx.android.synthetic.main.activity_sample.*
import kotlin.random.Random

class SampleActivity : AppCompatActivity() {

    companion object {
        private val ALL_CATEGORIES = listOf(
            RedCategory,
            GreenCategory,
            LavenderCategory
        )
    }

    private val dataItems = mutableListOf<DataItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        donut_view.cap = 5f
        initControls()
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
        }

        // Remove random amount from random category
        button_remove.setOnClickListener {
            if (dataItems.isNotEmpty()) {
                val randomIndex = dataItems.indexOf(dataItems.random())
                dataItems.removeAt(randomIndex)
                donut_view.submitData(dataItems.toDonutEntries())
            }
        }

        button_random_colors.setOnClickListener {
            // TODO
        }

        // Clear graph
        button_clear.setOnClickListener {
            dataItems.clear()
            donut_view.clear()
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
        amount = amount,
        color = ContextCompat.getColor(this@SampleActivity, category.colorRes)
    )
}
