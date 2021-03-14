package app.futured.donut.compose.data

import androidx.compose.ui.graphics.Color

/**
 * Data class representing the single section of the donut containing amount and color.
 *
 * @param amount value of this single section. Eg. when the [DonutModel.cap] is set to 10f and this is set to 2.5f
 * then this section will take 25% of the whole donut.
 * @param color color of this single section
 */
data class DonutSection(
    val amount: Float,
    val color: Color
)
