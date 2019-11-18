package com.thefuntasty.donut

/**
 * Data class representing dataset containing [Float] entries, color and name.
 * Entries with the same [name] are shown on same progress line.
 */
data class DonutDataset(
    val name: String,
    val color: Int,
    val entries: List<Float>
)
