package app.futured.donut

import android.graphics.Canvas
import android.graphics.ComposePathEffect
import android.graphics.CornerPathEffect
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import kotlin.math.ceil

internal class DonutProgressLine(
    val name: String,
    radius: Float,
    lineColor: Int,
    lineStrokeWidth: Float,
    masterProgress: Float,
    length: Float,
    gapWidthDegrees: Float,
    gapAngleDegrees: Float
) {

    companion object {
        const val SIDES = 64
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeWidth = mLineStrokeWidth
        color = mLineColor
    }

    internal var path: Path = createPath()

    var mRadius: Float = 0.0f
        set(value) {
            field = value
            updatePath()
            updatePathEffect()
        }

    var mLineColor: Int = 0
        set(value) {
            field = value
            paint.color = value
        }

    var mLineStrokeWidth: Float = 0.0f
        set(value) {
            field = value
            paint.strokeWidth = value
        }

    var mMasterProgress: Float = 0.0f
        set(value) {
            field = value
            updatePathEffect()
        }

    var mLength: Float = 0.0f
        set(value) {
            field = value
            updatePathEffect()
        }

    var mGapWidthDegrees = 10f
        set(value) {
            field = value
            updatePath()
            updatePathEffect()
        }

    var mGapAngleDegrees = 270f
        set(value) {
            field = value
            updatePath()
            updatePathEffect()
        }

    init {
        this.mRadius = radius
        this.mLineColor = lineColor
        this.mLineStrokeWidth = lineStrokeWidth
        this.mMasterProgress = masterProgress
        this.mLength = length
        this.mGapWidthDegrees = gapWidthDegrees
        this.mGapAngleDegrees = gapAngleDegrees
    }

    private fun createPath(): Path {
        val path = Path()

        val offset = mGapAngleDegrees.toRadians()

        val startAngle = 0.0 + (mGapWidthDegrees / 2f).toRadians()
        val endAngle = Math.PI * 2.0 - (mGapWidthDegrees / 2f).toRadians()
        val angleStep = (endAngle - startAngle) / SIDES

        path.moveTo(
            mRadius * Math.cos(startAngle + offset).toFloat(),
            mRadius * Math.sin(startAngle + offset).toFloat()
        )

        for (i in 1 until SIDES + 1) {
            path.lineTo(
                mRadius * Math.cos(i * angleStep + offset + startAngle).toFloat(),
                mRadius * Math.sin(i * angleStep + offset + startAngle).toFloat()
            )
        }

        return path
    }

    private fun updatePath() {
        this.path = createPath()
    }

    private fun updatePathEffect() {
        val pathLen = PathMeasure(path, false).length
        val drawnLength = ceil(pathLen.toDouble() * mLength * mMasterProgress).toFloat()

        paint.pathEffect = ComposePathEffect(
            CornerPathEffect(pathLen / SIDES),
            DashPathEffect(
                floatArrayOf(
                    drawnLength,
                    pathLen - drawnLength
                ),
                0f
            )
        )
    }

    fun draw(canvas: Canvas) {
        canvas.drawPath(path, paint)
    }

    private fun Float.toRadians() = Math.toRadians(this.toDouble())
}
