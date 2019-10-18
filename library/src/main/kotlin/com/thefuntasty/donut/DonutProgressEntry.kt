package com.thefuntasty.donut

import androidx.annotation.ColorInt

/**
 * Data class representing progress entry in specified [group].
 * Entries with same [group] are shown with the same progress line.
 * Line color is determined by [color] of first entry added.
 */
data class DonutProgressEntry(
    val category: String,
    val amount: Float,
    @ColorInt val color: Int
)
