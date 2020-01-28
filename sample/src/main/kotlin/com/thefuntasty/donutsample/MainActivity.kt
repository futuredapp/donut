package com.thefuntasty.donutsample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        android_view_sample_button.setOnClickListener {
            startActivity(Intent(this, SampleActivity::class.java))
        }

        compose_sample_button.setOnClickListener {
            startActivity(Intent(this, SampleComposeActivity::class.java))
        }
    }
}
