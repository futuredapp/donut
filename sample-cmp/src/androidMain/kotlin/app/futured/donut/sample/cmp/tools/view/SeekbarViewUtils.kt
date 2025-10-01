package app.futured.donut.sample.cmp.tools.view

import android.widget.SeekBar
import android.widget.TextView
import app.futured.donut.sample.cmp.tools.extensions.doOnProgressChange

fun setupSeekbar(
    seekBar: SeekBar,
    titleTextView: TextView,
    initProgress: Int,
    getTitleText: (progress: Int) -> String,
    onProgressChanged: (progress: Int) -> Unit
) {
    titleTextView.text = getTitleText(initProgress)
    seekBar.apply {
        progress = initProgress
        doOnProgressChange {
            onProgressChanged(progress)
            titleTextView.text = getTitleText(progress)
        }
    }
}
