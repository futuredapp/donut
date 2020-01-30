package app.futured.donutsample.ui.playground.compose

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.memo
import androidx.compose.unaryPlus
import androidx.ui.core.Dp
import androidx.ui.core.Draw
import androidx.ui.core.Modifier
import androidx.ui.core.Text
import androidx.ui.core.center
import androidx.ui.core.dp
import androidx.ui.core.setContent
import androidx.ui.core.toOffset
import androidx.ui.graphics.Color
import androidx.ui.graphics.Paint
import androidx.ui.layout.Column
import androidx.ui.layout.Container
import androidx.ui.layout.Gravity
import androidx.ui.layout.Row
import androidx.ui.layout.WidthSpacer
import androidx.ui.toStringAsFixed
import app.futured.donut.librarycompose.DonutConfig
import app.futured.donut.librarycompose.DonutProgress
import app.futured.donut.librarycompose.DonutProgressEntry
import app.futured.donut.librarycompose.DonutProgressLine
import app.futured.donutsample.R
import app.futured.donutsample.tools.extensions.sumByFloat
import app.futured.donutsample.tools.view.setupSeekbar
import kotlin.math.max
import kotlin.random.Random.Default.nextFloat
import kotlinx.android.synthetic.main.activity_playground_compose.*

class SampleComposeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_playground_compose)

        val config = DonutConfig(
            gapWidthDegrees = 270f,
            gapAngleDegrees = 90f,
            strokeWidth = 40f,
            backgroundLineColor = Color.LightGray
        )

        val data = DonutProgressLine(
            cap = 8f,
            masterProgress = 0f,
            progressEntries = listOf()
        )

        initControls(config, data)

        donut_view.setContent {
            Container(height = Dp(280f), width = Dp(280f)) {
                DonutProgress(config, data)
                Column {
                    val cap = data.progressEntries.sumByFloat { it.amount }.toStringAsFixed(2)
                    Text(text = "Amount cap: $cap", modifier = Gravity.Center)
                    Text(text = "Total amount: ${data.cap.toStringAsFixed(2)}", modifier = Gravity.Center)
                    Row(modifier = Gravity.Center) {
                        Circle(radius = 4.dp, color = Color.Cyan, modifier = Gravity.Center)
                        WidthSpacer(12.dp)
                        Text(text = data.progressEntries.getOrNull(0)?.amount?.toStringAsFixed(2) ?: "0.0")
                    }
                    Row(modifier = Gravity.Center) {
                        Circle(radius = 4.dp, color = Color.Red, modifier = Gravity.Center)
                        WidthSpacer(12.dp)
                        Text(text = data.progressEntries.getOrNull(1)?.amount?.toStringAsFixed(2) ?: "0.0")
                    }
                    Row(modifier = Gravity.Center) {
                        Circle(radius = 4.dp, color = Color.Green, modifier = Gravity.Center)
                        WidthSpacer(12.dp)
                        Text(text = data.progressEntries.getOrNull(2)?.amount?.toStringAsFixed(2) ?: "0.0")
                    }
                    Row(modifier = Gravity.Center) {
                        Circle(radius = 4.dp, color = Color.Blue, modifier = Gravity.Center)
                        WidthSpacer(12.dp)
                        Text(text = data.progressEntries.getOrNull(3)?.amount?.toStringAsFixed(2) ?: "0.0")
                    }
                }
            }
        }

        Handler().postDelayed({
            data.masterProgress = 1f
            data.progressEntries = listOf(
                DonutProgressEntry(amount = 2f, color = Color.Cyan),
                DonutProgressEntry(amount = 2f, color = Color.Red),
                DonutProgressEntry(amount = 2f, color = Color.Green),
                DonutProgressEntry(amount = 0f, color = Color.Blue)
            )
        }, 600)
    }

    private fun initControls(config: DonutConfig, data: DonutProgressLine) {
        setupSeekbar(
            seekBar = master_progress_seekbar,
            titleTextView = master_progress_text,
            initProgress = 100,
            getTitleText = { getString(R.string.master_progress, it) },
            onProgressChanged = { data.masterProgress = it / 100f }
        )

        setupSeekbar(
            seekBar = gap_width_seekbar,
            titleTextView = gap_width_text,
            initProgress = 270,
            getTitleText = { getString(R.string.gap_width, it) },
            onProgressChanged = { config.gapWidthDegrees = it.toFloat() }
        )

        setupSeekbar(
            seekBar = gap_angle_seekbar,
            titleTextView = gap_angle_text,
            initProgress = 90,
            getTitleText = { getString(R.string.gap_angle, it) },
            onProgressChanged = { config.gapAngleDegrees = it.toFloat() }
        )

        setupSeekbar(
            seekBar = stroke_width_seekbar,
            titleTextView = stroke_width_text,
            initProgress = 30,
            getTitleText = { getString(R.string.stroke_width, it) },
            onProgressChanged = { config.strokeWidth = it.toFloat() }
        )

        setupSeekbar(
            seekBar = cap_seekbar,
            titleTextView = cap_text,
            initProgress = 4,
            getTitleText = { getString(R.string.amount_cap, it.toFloat()) },
            onProgressChanged = {
                data.cap = it.toFloat()
            }
        )

        button_add.setOnClickListener {
            data.progressEntries.random().apply {
                amount += nextFloat()
            }
        }

        button_remove.setOnClickListener {
            for (entry in data.progressEntries.shuffled()) {
                if (entry.amount != 0f) {
                    entry.amount = max(0f, entry.amount - nextFloat())
                    break
                }
            }
        }

        button_random_values.setOnClickListener {
            data.progressEntries.forEach {
                it.amount = nextFloat() + nextFloat()
            }
        }

        button_random_colors.setOnClickListener {
            data.progressEntries.forEach {
                it.color = Color(nextFloat(), nextFloat(), nextFloat())
            }
        }

        button_clear.setOnClickListener {
            data.progressEntries.forEach {
                it.amount = 0f
            }
        }
    }
}

@Composable
fun Circle(radius: Dp, color: Color, modifier: Modifier = Modifier.None) {
    val paint = +memo { Paint() }
    paint.color = color
    Container(width = radius, height = radius, modifier = modifier) {
        Draw { canvas, parentSize ->
            canvas.drawCircle(parentSize.center().toOffset(), radius.toIntPx().value.toFloat(), paint)
        }
    }
}
