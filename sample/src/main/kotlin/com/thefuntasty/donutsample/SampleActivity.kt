package com.thefuntasty.donutsample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.thefuntasty.donut.DonutProgressEntry
import kotlinx.android.synthetic.main.activity_sample.*

class SampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        donut_view.apply {
            cap = 5f
            submitEntries(
                listOf(
                    DonutProgressEntry("broccoli", 1.5f, getColorCompat(R.color.dark_green)),
                    DonutProgressEntry("avocado", 1.2f, getColorCompat(R.color.light_green)),
                    DonutProgressEntry("eggplant", 0.5f, getColorCompat(R.color.light_violet))
                )
            )
        }
    }

    private fun AppCompatActivity.getColorCompat(id: Int) = ContextCompat.getColor(this, id)
}
