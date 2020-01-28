package app.futured.donut.librarycompose

import androidx.compose.Composable
import androidx.compose.memo
import androidx.compose.unaryPlus
import androidx.ui.animation.animatedFloat
import androidx.ui.core.Draw
import androidx.ui.core.PxSize
import androidx.ui.core.center
import androidx.ui.core.min
import androidx.ui.engine.geometry.Offset
import androidx.ui.engine.geometry.Rect
import androidx.ui.graphics.*
import app.futured.donut.librarycompose.utilities.sumByFloat
import kotlin.math.max

@Composable
fun DonutProgress(config: DonutConfig, data: DonutProgressLine) {
    check(data.progressEntries.size <= config.maxSegments) {
        "Number of segments (${data.progressEntries.size}) exceeds maxSegments (${config.maxSegments})"
    }

    val gap = config.gap.coerceIn(0f, 360f)
    val animatedGap = +animatedFloat(gap)
    animatedGap.animateTo(gap)

    val animatedRotation = +animatedFloat(config.rotation)
    animatedRotation.animateTo(config.rotation)

    val animatedStrokeWidth = +animatedFloat(config.strokeWidth)
    animatedStrokeWidth.animateTo(config.strokeWidth)

    val animatedTotalAmount = +animatedFloat(data.totalAmount)
    animatedTotalAmount.animateTo(data.totalAmount)

    val totalAmountProgress = data.totalAmountProgress.coerceIn(0f, 1f)
    val animatedTotalAmountProgress = +animatedFloat(0f)
    animatedTotalAmountProgress.animateTo(totalAmountProgress)

    val segments = addRemainingDonutSegments(config, data.progressEntries)
    val allSegmentsAmount = segments.sumByFloat { it.amount }
    val wholeDonutAmount = max(animatedTotalAmount.value, allSegmentsAmount)
    val wholeDonutAngle = 360f - animatedGap.value
    val masterSegmentAmount = wholeDonutAmount * animatedTotalAmountProgress.value
    val masterSegmentAngle = wholeDonutAngle * animatedTotalAmountProgress.value

    val halfAnimatedGap = animatedGap.value / 2
    val startAngle = animatedRotation.value + halfAnimatedGap
    val totalPathData = DonutPathData(config.totalAmountColor, startAngle, masterSegmentAngle)
    val segmentsPathData = createDonutPathDataForSegments(
        startAngle,
        masterSegmentAmount,
        masterSegmentAngle,
        totalAmountProgress,
        segments
    )

    val paint = +memo {
        Paint().apply {
            strokeCap = StrokeCap.round
            style = PaintingStyle.stroke
        }
    }
    paint.strokeWidth = animatedStrokeWidth.value

    Draw { canvas, parentSize ->
        drawDonutSegment(canvas, parentSize, paint, totalPathData)
        segmentsPathData.reversed().forEach { pathData ->
            drawDonutSegment(canvas, parentSize, paint, pathData)
        }
    }
}

private fun addRemainingDonutSegments(config: DonutConfig, progressEntries: List<DonutProgressEntry>): List<DonutProgressEntry> {
    val remainingSegmentsSize = config.maxSegments - progressEntries.size
    return mutableListOf<DonutProgressEntry>().apply {
        addAll(progressEntries)
        for (i in 0..remainingSegmentsSize) {
            add(DonutProgressEntry(0f, Color.Transparent))
        }
    }
}

private fun createDonutPathDataForSegments(startAngle: Float,
    masterSegmentAmount: Float,
    masterSegmentAngle: Float,
    animatedTotalAmountProgress: Float,
    progressEntries: List<DonutProgressEntry>
) : List<DonutPathData> {
    var angleAccumulator = startAngle
    return progressEntries.map { segment ->
        val segmentAngle = if (masterSegmentAmount != 0f) {
            masterSegmentAngle * (segment.amount / masterSegmentAmount) * animatedTotalAmountProgress
        } else {
            0f
        }

        val animatedSegmentAmount = +animatedFloat(segmentAngle)
        animatedSegmentAmount.animateTo(segmentAngle)

        val segmentData = DonutPathData(
            color = segment.color,
            startAngle = angleAccumulator,
            sweepAngle = animatedSegmentAmount.value
        )

        angleAccumulator += animatedSegmentAmount.value

        segmentData
    }
}

private fun drawDonutSegment(canvas: Canvas, parentSize: PxSize, paint: Paint, donutPathData: DonutPathData) {
    val maxSize = min(parentSize.height, parentSize.width).value
    val center = Offset(parentSize.center().x.value, parentSize.center().y.value)
    val radius = (maxSize / 2) - (paint.strokeWidth / 2)
    val rect = Rect.fromCircle(center, radius)

    paint.color = donutPathData.color
    canvas.drawArc(rect, donutPathData.startAngle, donutPathData.sweepAngle, false, paint)
}

private data class DonutPathData(
    val color: Color,
    val startAngle: Float,
    val sweepAngle: Float
)
