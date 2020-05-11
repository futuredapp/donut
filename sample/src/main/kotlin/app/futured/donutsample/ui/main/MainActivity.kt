package app.futured.donutsample.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import app.futured.donutsample.R
import app.futured.donutsample.ui.playground.PlaygroundActivity
import app.futured.donutsample.ui.playground.compose.PlaygroundComposeActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.android_view_sample_button).setOnClickListener {
            startActivity(Intent(this, PlaygroundActivity::class.java))
        }

        findViewById<View>(R.id.compose_sample_button).setOnClickListener {
            startActivity(Intent(this, PlaygroundComposeActivity::class.java))
        }
    }
}
