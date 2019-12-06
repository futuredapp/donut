package app.futured.donut

/**
 * Data class representing dataset containing [Float] amount, name and color of progress line.
 */
data class DonutDataset(
    val name: String,
    val color: Int,
    val amount: Float
)
