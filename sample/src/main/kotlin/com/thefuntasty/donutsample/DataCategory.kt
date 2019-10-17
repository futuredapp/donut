package com.thefuntasty.donutsample

import androidx.annotation.ColorRes

sealed class DataCategory(val name: String, @ColorRes val colorRes: Int)

object RedCategory : DataCategory("red", R.color.furious_red)
object GreenCategory : DataCategory("green", R.color.process_green)
object LavenderCategory : DataCategory("lavender", R.color.lavender)
