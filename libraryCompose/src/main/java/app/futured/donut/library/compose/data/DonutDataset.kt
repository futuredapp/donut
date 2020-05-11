package app.futured.donut.library.compose.data

import androidx.compose.Model
import androidx.ui.graphics.Color

/**
 * Data class representing the single section of the donut containing amount and color.
 *
 * @param amount value of this single section. Eg. when the [DonutData.cap] is set to 10f and this is set to 2.5f
 * then this section will take 25% of the whole donut.
 * @param color color of this single section
 */
@Model data class DonutDataset(
    var amount: Float,
    var color: Color
)
