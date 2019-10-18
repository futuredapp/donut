package com.thefuntasty.donutsample.tools.extensions

import com.thefuntasty.donut.DonutProgressEntry
import com.thefuntasty.donutsample.data.model.DataItem

fun List<DataItem>.toDonutEntries() = map { it.toDonutEntry() }

fun DataItem.toDonutEntry() = DonutProgressEntry(
    category = category.name,
    amount = amount
)
