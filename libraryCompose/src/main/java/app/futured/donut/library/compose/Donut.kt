package app.futured.donut.library.compose

import androidx.compose.Composable
import androidx.compose.remember
import androidx.ui.animation.animatedColor
import androidx.ui.animation.animatedFloat
import androidx.ui.core.Modifier
import androidx.ui.foundation.Canvas
import androidx.ui.geometry.Offset
import androidx.ui.geometry.Rect
import androidx.ui.graphics.Canvas
import androidx.ui.graphics.Paint
import androidx.ui.graphics.PaintingStyle
import androidx.ui.graphics.StrokeCap
import androidx.ui.layout.fillMaxSize
import androidx.ui.unit.PxSize
import androidx.ui.unit.center
import androidx.ui.unit.min
import app.futured.donut.library.compose.data.DonutConfig
import app.futured.donut.library.compose.data.DonutData
import app.futured.donut.library.compose.internal.data.DatasetsPathData
import app.futured.donut.library.compose.internal.data.DonutPathData
import app.futured.donut.library.compose.internal.data.DonutPathDataEntry
import app.futured.donut.library.compose.internal.data.DonutProgressValues
import app.futured.donut.library.compose.internal.extensions.animateOrSnapDistinctValues
import kotlin.math.max

/**
 * [DonutProgress] is a composable Android view that helps you to easily create doughnut-like charts with fully
 * customizable animations.
 *
 * @param data data used to draw the content of the Donut
 * @param config configuration used to define animations setup of the Donut
 */
@Composable fun DonutProgress(data: DonutData, config: DonutConfig = DonutConfig(), modifier: Modifier = Modifier.fillMaxSize()) {
    assertDatasetSizeUnchanged(data)

    val adjustedData = adjustData(data)
    val donutProgressValues = createDonutProgressValues(adjustedData)
    animateOrSnapDistinctValues(adjustedData, config, donutProgressValues)

    DrawDonut(adjustedData, donutProgressValues, modifier)
}

@Composable private fun createDonutProgressValues(data: DonutData): DonutProgressValues {
    val pathData = calculatePathData(data)
    val animatedGapAngle = animatedFloat(data.gapAngleDegrees)
    val animatedMasterProgress = animatedFloat(data.masterProgress)
    val animatedGapWidthDegrees = animatedFloat(data.gapWidthDegrees)
    val animatedStrokeWidth = animatedFloat(data.strokeWidth)
    val animatedBackgroundLineColor = animatedColor(data.backgroundLineColor)
    val animatedCap = animatedFloat(data.cap)
    val animatedProgressStartAngles = data.datasets.mapIndexed { index, _ -> animatedFloat(pathData[index].startAngle) }
    val animatedProgressSweepAngles = data.datasets.mapIndexed { index, _ -> animatedFloat(pathData[index].sweepAngle) }
    val animatedProgressColors = data.datasets.map { animatedColor(it.color) }

    return DonutProgressValues(
        animatedGapAngle = animatedGapAngle,
        animatedMasterProgress = animatedMasterProgress,
        animatedGapWidthDegrees = animatedGapWidthDegrees,
        animatedStrokeWidth = animatedStrokeWidth,
        animatedBackgroundLineColor = animatedBackgroundLineColor,
        animatedCap = animatedCap,
        animatedStartAngles = animatedProgressStartAngles,
        animatedSweepAngles = animatedProgressSweepAngles,
        animatedColors = animatedProgressColors,
        pathData = pathData
    )
}

@Composable private fun DrawDonut(data: DonutData, donutProgressValues: DonutProgressValues, modifier: Modifier) {
    val masterProgress = donutProgressValues.animatedMasterProgress.value
    val gapAngleDegrees = donutProgressValues.animatedGapAngle.value
    val gapWidthDegrees = donutProgressValues.animatedGapWidthDegrees.value
    val backgroundLineColor = donutProgressValues.animatedBackgroundLineColor.value

    val wholeDonutAngle = 360f - gapWidthDegrees
    val masterSegmentAngle = wholeDonutAngle * masterProgress
    val startAngle = gapAngleDegrees + gapWidthDegrees / 2

    val masterPathData = DonutPathDataEntry(backgroundLineColor, startAngle, masterSegmentAngle)
    val entriesPathData = mutableListOf<DonutPathDataEntry>().apply {
        data.datasets.forEachIndexed { index, _ ->
            this += DonutPathDataEntry(
                color = donutProgressValues.animatedColors[index].value,
                startAngle = donutProgressValues.animatedStartAngles[index].value,
                sweepAngle = donutProgressValues.animatedSweepAngles[index].value
            )
        }
    }
    val donutPathData = DonutPathData(masterPathData, entriesPathData)

    val paint = getMemoizedPaint(donutProgressValues.animatedStrokeWidth.value)
    Canvas(modifier = modifier, onCanvas = {
        drawDonutSegment(size, paint, donutPathData.masterPathData)
        donutPathData.entriesPathData.forEach { pathData ->
            drawDonutSegment(size, paint, pathData)
        }
    })
}

@Composable private fun getMemoizedPaint(strokeWidth: Float): Paint {
    return remember {
        Paint().apply {
            strokeCap = StrokeCap.round
            style = PaintingStyle.stroke
        }
    }.apply {
        this.strokeWidth = strokeWidth
    }
}

@Composable private fun assertDatasetSizeUnchanged(data: DonutData) {
    val initialDatasetsCount = remember { data.datasets.size }
    check(data.datasets.size <= initialDatasetsCount) {
        "Adding or removing datasets is not supported." +
        "Instead of adding new datasets dynamically add empty datasets during initialization and " +
        "instead of removing existing datasets dynamically set datasets value to zero."
    }
}

private fun animateOrSnapDistinctValues(data: DonutData, config: DonutConfig, donutProgressValues: DonutProgressValues) {
    donutProgressValues.animatedGapAngle.animateOrSnapDistinctValues(
        newValue = data.gapAngleDegrees,
        isAnimationEnabled = config.isGapAngleAnimationEnabled,
        animationBuilder = config.gapAngleAnimationBuilder
    )
    donutProgressValues.animatedMasterProgress.animateOrSnapDistinctValues(
        newValue = data.masterProgress,
        isAnimationEnabled = config.isMasterProgressAnimationEnabled,
        animationBuilder = config.masterProgressAnimationBuilder
    )
    donutProgressValues.animatedGapWidthDegrees.animateOrSnapDistinctValues(
        newValue = data.gapWidthDegrees,
        isAnimationEnabled = config.isGapWidthAnimationEnabled,
        animationBuilder = config.gapWidthAnimationBuilder
    )
    donutProgressValues.animatedStrokeWidth.animateOrSnapDistinctValues(
        newValue = data.strokeWidth,
        isAnimationEnabled = config.isStrokeWidthAnimationEnabled,
        animationBuilder = config.strokeWidthAnimationBuilder
    )
    donutProgressValues.animatedStrokeWidth.animateOrSnapDistinctValues(
        newValue = data.strokeWidth,
        isAnimationEnabled = config.isStrokeWidthAnimationEnabled,
        animationBuilder = config.strokeWidthAnimationBuilder
    )
    donutProgressValues.animatedBackgroundLineColor.animateOrSnapDistinctValues(
        newValue = data.backgroundLineColor,
        isAnimationEnabled = config.isBackgroundLineColorAnimationEnabled,
        animationBuilder = config.backgroundLineColorAnimationBuilder
    )
    donutProgressValues.animatedCap.animateOrSnapDistinctValues(
        newValue = data.cap,
        isAnimationEnabled = config.isCapAnimationEnabled,
        animationBuilder = config.capAnimationBuilder
    )
    donutProgressValues.pathData.forEachIndexed { index, donutPathDataEntry ->
        donutProgressValues.animatedColors[index].animateOrSnapDistinctValues(
            newValue = donutPathDataEntry.color,
            isAnimationEnabled = config.isDatasetColorAnimationEnabled,
            animationBuilder = config.datasetColorAnimationBuilder
        )
        donutProgressValues.animatedSweepAngles[index].animateOrSnapDistinctValues(
            newValue = donutPathDataEntry.sweepAngle,
            isAnimationEnabled = config.isDatasetAmountAnimationEnabled,
            animationBuilder = config.datasetAmountAnimationBuilder
        )
        donutProgressValues.animatedStartAngles[index].animateOrSnapDistinctValues(
            newValue = donutPathDataEntry.startAngle,
            isAnimationEnabled = config.isDatasetAmountAnimationEnabled,
            animationBuilder = config.datasetAmountAnimationBuilder
        )
    }
}

private fun adjustData(data: DonutData) = data.copy(
    gapAngleDegrees = data.gapAngleDegrees.coerceIn(0f, 360f),
    masterProgress = data.masterProgress.coerceIn(0f, 1f),
    gapWidthDegrees = data.gapWidthDegrees.coerceIn(0f, 360f)
)

private fun calculatePathData(data: DonutData): List<DonutPathDataEntry> {
    val allEntriesAmount = data.datasetsCap
    val wholeDonutAmount = max(data.cap, allEntriesAmount)
    val masterSegmentAmount = wholeDonutAmount * data.masterProgress
    val wholeDonutAngle = 360f - data.gapWidthDegrees
    val masterSegmentAngle = wholeDonutAngle * data.masterProgress
    val halfGap = data.gapWidthDegrees / 2
    val startAngle = data.gapAngleDegrees + halfGap
    return createPathDataForDatasets(
        DatasetsPathData(
            startAngle = startAngle,
            masterSegmentAmount = masterSegmentAmount,
            masterSegmentAngle = masterSegmentAngle,
            masterProgress = data.masterProgress,
            datasets = data.datasets
        )
    )
}

private fun createPathDataForDatasets(data: DatasetsPathData): List<DonutPathDataEntry> = with(data) {
    var angleAccumulator = startAngle
    val entriesPathData = mutableListOf<DonutPathDataEntry>()

    for (entry in datasets) {
        val entryAngle = if (masterSegmentAmount != 0f) {
            masterSegmentAngle * (entry.amount / masterSegmentAmount) * masterProgress
        } else {
            0f
        }

        entriesPathData += DonutPathDataEntry(
            color = entry.color,
            startAngle = angleAccumulator,
            sweepAngle = entryAngle
        )

        angleAccumulator += entryAngle
    }

    return entriesPathData.reversed()
}

private fun Canvas.drawDonutSegment(parentSize: PxSize, paint: Paint, data: DonutPathDataEntry) {
    val maxSize = min(parentSize.height, parentSize.width).value
    val radius = maxSize / 2 - paint.strokeWidth / 2
    val center = Offset(parentSize.center().x.value, parentSize.center().y.value)
    val rect = Rect.fromCircle(center, radius)
    paint.color = data.color
    drawArc(rect, data.startAngle, data.sweepAngle, false, paint)
}
