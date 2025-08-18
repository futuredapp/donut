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
    lineStrokeCap: DonutStrokeCap,
    masterProgress: Float,
    length: Float,
    gapWidthDegrees: Float,
    gapAngleDegrees: Float,
    direction: DonutDirection
) {

    companion object {
        const val SIDES = 64
    }

    var mLineStrokeWidth: Float = 0.0f
        set(value) {
            field = value
            paint.strokeWidth = value
        }

    var mLineColor: Int = 0
        set(value) {
            field = value
            paint.color = value
        }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = lineStrokeCap.cap
        strokeWidth = mLineStrokeWidth
        color = mLineColor
    }

    var mRadius: Float = 0.0f
        set(value) {
            field = value
            updatePath()
            updatePathEffect()
        }

    var mLineStrokeCap: DonutStrokeCap = DonutStrokeCap.ROUND
        set(value) {
            field = value
            paint.strokeCap = value.cap
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

    var mDirection = DonutDirection.CLOCKWISE
        set(value) {
            field = value
            updatePath()
            updatePathEffect()
        }

    private var path: Path = createPath()

    init {
        this.mRadius = radius
        this.mLineColor = lineColor
        this.mLineStrokeWidth = lineStrokeWidth
        this.mLineStrokeCap = lineStrokeCap
        this.mMasterProgress = masterProgress
        this.mLength = length
        this.mGapWidthDegrees = gapWidthDegrees
        this.mGapAngleDegrees = gapAngleDegrees
        this.mDirection = direction
    }

    private fun createPath(): Path {
        val path = Path()

        val offset = mGapAngleDegrees.toRadians()

        val startAngle = when (mDirection) {
            DonutDirection.CLOCKWISE -> 0.0 + (mGapWidthDegrees / 2f).toRadians()
            DonutDirection.ANTICLOCKWISE -> Math.PI * 2.0 - (mGapWidthDegrees / 2f).toRadians()
        }
        val endAngle = when (mDirection) {
            DonutDirection.CLOCKWISE -> Math.PI * 2.0 - (mGapWidthDegrees / 2f).toRadians()
            DonutDirection.ANTICLOCKWISE -> 0.0 + (mGapWidthDegrees / 2f).toRadians()
        }
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
