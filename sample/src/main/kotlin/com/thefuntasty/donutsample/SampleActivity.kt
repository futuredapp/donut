package com.thefuntasty.donutsample

import android.os.Bundle
import android.os.Handler
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.thefuntasty.donut.DonutProgressEntry
import kotlinx.android.synthetic.main.activity_sample.*

class SampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        donut_view.cap = 5f
        initControls()
        runDelayed(1000) { fillData() }
    }

    private fun fillData() {
        donut_view.apply {
            submitEntries(
                listOf(
                    DonutProgressEntry("broccoli", 1.5f, getColorCompat(R.color.dark_green)),
                    DonutProgressEntry("avocado", 1.2f, getColorCompat(R.color.light_green)),
                    DonutProgressEntry("eggplant", 0.5f, getColorCompat(R.color.light_violet))
                )
            )
        }
    }

    private fun initControls() {
        master_progress_text.text = getString(
            R.string.master_progress,
            (donut_view.masterProgress * 100f).toInt()
        )
        master_progress_seekbar.apply {
            max = 100
            progress = (donut_view.masterProgress * 100f).toInt()
            doOnProgressChange {
                donut_view.masterProgress = it / 100f
                master_progress_text.text = getString(R.string.master_progress, it)
            }
        }

        gap_width_text.text = getString(R.string.gap_width, donut_view.gapWidthDegrees.toInt())
        gap_width_seekbar.apply {
            max = 360
            progress = donut_view.gapWidthDegrees.toInt()
            doOnProgressChange {
                donut_view.gapWidthDegrees = it.toFloat()
                gap_width_text.text = getString(R.string.gap_width, it)
            }
        }

        gap_angle_text.text = getString(R.string.gap_angle, donut_view.gapAngleDegrees.toInt())
        gap_angle_seekbar.apply {
            max = 360
            progress = donut_view.gapAngleDegrees.toInt()
            doOnProgressChange {
                donut_view.gapAngleDegrees = it.toFloat()
                gap_angle_text.text = getString(R.string.gap_angle, it)
            }
        }

        stroke_width_text.text = getString(R.string.stroke_width, donut_view.strokeWidth.toInt())
        stroke_width_seekbar.apply {
            max = 100
            progress = donut_view.strokeWidth.toInt()
            doOnProgressChange {
                donut_view.strokeWidth = it.toFloat()
                stroke_width_text.text = getString(R.string.stroke_width, it)
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
}
