package app.futured.donutsample.tools.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

fun AppCompatActivity.getColorCompat(id: Int) = ContextCompat.getColor(this, id)
