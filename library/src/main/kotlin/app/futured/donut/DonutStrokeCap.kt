package app.futured.donut

import android.graphics.Paint

/**
 * Enum to specify a stroke cap that will be used to draw donut lines.
 */
enum class DonutStrokeCap(val index: Int, val cap: Paint.Cap) {
    ROUND(index = 0, cap = Paint.Cap.ROUND),
    BUTT(index = 1, cap = Paint.Cap.BUTT)
}
