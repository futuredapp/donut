package com.thefuntasty.donut

import androidx.annotation.ColorInt

/**
 * Data class representing progress entry in [DonutProgressView].
 */
data class DonutProgressEntry(
    val category: String,
    val amount: Float,
    @ColorInt val color: Int
)
