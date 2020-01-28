package com.thefuntasty.donutsample

import android.os.Bundle
import android.os.Handler
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.ui.core.Dp
import androidx.ui.core.setContent
import androidx.ui.graphics.Color
import androidx.ui.layout.Container
import app.futured.donut.librarycompose.DonutProgress
import app.futured.donut.librarycompose.DonutConfig
import app.futured.donut.librarycompose.DonutProgressEntry
import app.futured.donut.librarycompose.DonutProgressLine
import kotlinx.android.synthetic.main.activity_sample.gap_angle_seekbar
import kotlinx.android.synthetic.main.activity_sample.gap_angle_text
import kotlinx.android.synthetic.main.activity_sample.gap_width_seekbar
import kotlinx.android.synthetic.main.activity_sample.gap_width_text
import kotlinx.android.synthetic.main.activity_sample.master_progress_seekbar
import kotlinx.android.synthetic.main.activity_sample.master_progress_text
import kotlinx.android.synthetic.main.activity_sample.stroke_width_seekbar
import kotlinx.android.synthetic.main.activity_sample.stroke_width_text
import kotlinx.android.synthetic.main.activity_sample_compose.*
import kotlin.random.Random.Default.nextFloat

class SampleComposeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sample_compose)

        val config = DonutConfig(
            rotation = 90f,
            gap = 90f,
            strokeWidth = 40f
        )

        val data = DonutProgressLine(
            totalAmount = 10f,
            totalAmountProgress = 0f,
            progressEntries = listOf()
        )

        initControls(config, data)

        donut_container.setContent {
            Container(height = Dp(200f), width = Dp(200f)) {
                DonutProgress(config, data)
            }
        }

        runDelayed(1000) {
            data.totalAmountProgress = 1f
            data.progressEntries = listOf(
                DonutProgressEntry(amount = 2f, color = Color.Cyan),
                DonutProgressEntry(amount = 2f, color = Color.Red),
                DonutProgressEntry(amount = 2f, color = Color.Green)
            )
        }
    }

    private fun initControls(config: DonutConfig, data: DonutProgressLine) {
        val masterProgressDefault = 100
        master_progress_text.text = getString(R.string.master_progress, masterProgressDefault)
        master_progress_seekbar.apply {
            max = 100
            progress = masterProgressDefault
            doOnProgressChange {
                data.totalAmountProgress = it / 100f
                master_progress_text.text = getString(R.string.master_progress, it)
            }
        }

        val gapWidthDefault = 90
        gap_width_text.text = getString(R.string.gap_width, gapWidthDefault)
        gap_width_seekbar.apply {
            max = 360
            progress = gapWidthDefault
            doOnProgressChange {
                config.gap = it.toFloat()
                gap_width_text.text = getString(R.string.gap_width, it)
            }
        }

        val gapAngleDefault = 90
        gap_angle_text.text = getString(R.string.gap_angle, gapAngleDefault)
        gap_angle_seekbar.apply {
            max = 360
            progress = gapAngleDefault
            doOnProgressChange {
                config.rotation = it.toFloat()
                gap_angle_text.text = getString(R.string.gap_angle, it)
            }
        }

        val strokeWidthDefault = 30
        stroke_width_text.text = getString(R.string.stroke_width, strokeWidthDefault)
        stroke_width_seekbar.apply {
            max = 100
            progress = strokeWidthDefault
            doOnProgressChange {
                config.strokeWidth = it.toFloat()
                stroke_width_text.text = getString(R.string.stroke_width, it)
            }
        }

        add_button.setOnClickListener {
            val randomColor = Color(nextFloat(), nextFloat(), nextFloat())
            val newItem = DonutProgressEntry(amount = 1f, color = randomColor)
            data.progressEntries = data.progressEntries + newItem
        }

        remove_button.setOnClickListener {
            data.progressEntries = data.progressEntries.toMutableList().apply {
                if (data.progressEntries.isNotEmpty()) {
                    removeAt(data.progressEntries.size - 1)
                }
            }
        }

        random_button.setOnClickListener {
            data.progressEntries.forEach {
                it.amount = nextFloat() + nextFloat()
            }
        }
    }

    private fun runDelayed(delayMs: Long, r: () -> Unit) = Handler().postDelayed(r, delayMs)

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
