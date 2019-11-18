package com.thefuntasty.donut

/**
 * Data class representing progress entry in specified [category].
 * Entries with same [category] are shown with the same progress line.
 */
data class DonutDataset(
    val name: String,
    val color: Int,
    val entries: List<Float>
)
