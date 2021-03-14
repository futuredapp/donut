package app.futured.donutsample.tools.extensions

import android.widget.RadioGroup

fun RadioGroup.checkedButtonIndex(): Int {
    for (index in 0 until this.childCount) {
        if (getChildAt(index).id == this.checkedRadioButtonId) {
            return index
        }
    }

    error("Unexpected: No button checked")
}