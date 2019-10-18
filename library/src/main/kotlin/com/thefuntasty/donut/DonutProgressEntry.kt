package com.thefuntasty.donut

/**
 * Data class representing progress entry in specified [group].
 * Entries with same [group] are shown with the same progress line.
 */
data class DonutProgressEntry(
    val category: String,
    val amount: Float
)
