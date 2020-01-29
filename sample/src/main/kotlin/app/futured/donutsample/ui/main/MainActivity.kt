package app.futured.donutsample.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.futured.donutsample.R
import app.futured.donutsample.ui.playground.PlaygroundActivity
import app.futured.donutsample.ui.playground.compose.SampleComposeActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        android_view_sample_button.setOnClickListener {
            startActivity(Intent(this, PlaygroundActivity::class.java))
        }

        compose_sample_button.setOnClickListener {
            startActivity(Intent(this, SampleComposeActivity::class.java))
        }
    }
}
