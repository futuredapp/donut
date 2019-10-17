package com.thefuntasty.donut

import android.graphics.Canvas
import android.graphics.ComposePathEffect
import android.graphics.CornerPathEffect
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure

internal class DonutProgressLine(
    val category: String,
    _radius: Float,
    _lineColor: Int,
    _lineStrokeWidth: Float,
    _masterProgress: Float,
    _length: Float,
    _gapWidthDegrees: Float,
    _gapAngleDegrees: Float
) {

    companion object {
        const val SIDES = 64
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeWidth = lineStrokeWidth
        color = lineColor
    }

    private var path: Path = createPath()

    var radius: Float = 0.0f
        set(value) {
            field = value
            updatePath()
            updatePathEffect()
        }

    var lineColor: Int = 0
        set(value) {
            field = value
            paint.color = value
        }

    var lineStrokeWidth: Float = 0.0f
        set(value) {
            field = value
            paint.strokeWidth = value
        }

    var masterProgress: Float = 0.0f
        set(value) {
            field = value
            updatePathEffect()
        }

    var length: Float = 0.0f
        set(value) {
            field = value
            updatePathEffect()
        }

    var gapWidthDegrees = 10f
        set(value) {
            field = value
            updatePath()
            updatePathEffect()
        }

    var gapAngleDegrees = 270f
        set(value) {
            field = value
            updatePath()
            updatePathEffect()
        }

    init {
        this.radius = _radius
        this.lineColor = _lineColor
        this.lineStrokeWidth = _lineStrokeWidth
        this.masterProgress = _masterProgress
        this.length = _length
        this.gapWidthDegrees = _gapWidthDegrees
        this.gapAngleDegrees = _gapAngleDegrees
    }

    private fun createPath(): Path {
        val path = Path()

        val offset = gapAngleDegrees.toRadians()

        val startAngle = 0.0 + (gapWidthDegrees / 2f).toRadians()
        val endAngle = Math.PI * 2.0 - (gapWidthDegrees / 2f).toRadians()
        val angleStep = (endAngle - startAngle) / SIDES

        path.moveTo(
            radius * Math.cos(startAngle + offset).toFloat(),
            radius * Math.sin(startAngle + offset).toFloat()
        )

        for (i in 1 until SIDES + 1) {
            path.lineTo(
                radius * Math.cos(i * angleStep + offset + startAngle).toFloat(),
                radius * Math.sin(i * angleStep + offset + startAngle).toFloat()
            )
        }

        return path
    }

    private fun updatePath() {
        this.path = createPath()
    }

    private fun updatePathEffect() {
        val pathLen = PathMeasure(path, false).length
        val drawnLength = Math.ceil((pathLen.toDouble() * length) * masterProgress).toFloat()

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
