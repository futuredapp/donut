package app.futured.donut

/**
 * Data class representing section of the graph containing [Float] amount, name and color of progress line.
 */
data class DonutSection(
    val name: String,
    val color: Int,
    val amount: Float
)
