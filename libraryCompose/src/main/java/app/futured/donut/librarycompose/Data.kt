package app.futured.donut.librarycompose

import androidx.compose.Model
import androidx.ui.graphics.Color

@Model
data class DonutConfig(
    var rotation: Float = 90f,
    var gap: Float = 90f,
    var strokeWidth: Float = 30f,
    var totalAmountColor: Color = Color.LightGray,
    val maxSegments: Int = 20
)

@Model
data class DonutProgressLine(
    var totalAmount: Float,
    var totalAmountProgress: Float = 1f,
    var progressEntries: List<DonutProgressEntry>
)

@Model
data class DonutProgressEntry(
    var amount: Float,
    var color: Color
)
