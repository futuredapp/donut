package app.futured.donut.librarycompose

import androidx.compose.Model
import androidx.ui.graphics.Color

@Model
data class DonutConfig(
    var gapWidthDegrees: Float = 90f,
    var gapAngleDegrees: Float = 90f,
    var strokeWidth: Float = 30f,
    var backgroundLineColor: Color = Color.LightGray,
    val maxSegments: Int = 20
)

@Model
data class DonutProgressLine(
    var cap: Float,
    var masterProgress: Float = 1f,
    var progressEntries: List<DonutProgressEntry>
)

@Model
data class DonutProgressEntry(
    var amount: Float,
    var color: Color
)
