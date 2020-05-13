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
import app.futured.donut.library.compose.data.DonutModel
import app.futured.donut.library.compose.internal.data.DonutPathData
import app.futured.donut.library.compose.internal.data.DonutPathDataEntry
import app.futured.donut.library.compose.internal.data.DonutProgressValues
import app.futured.donut.library.compose.internal.data.SectionsPathData
import app.futured.donut.library.compose.internal.extensions.animateOrSnapDistinctValues
import kotlin.math.max

/**
 * [DonutProgress] is a composable Android view that helps you to easily create doughnut-like charts with fully
 * customizable animations.
 *
 * @param model data used to draw the content of the Donut
 * @param config configuration used to define animations setup of the Donut
 */
@Composable fun DonutProgress(model: DonutModel, config: DonutConfig = DonutConfig(), modifier: Modifier = Modifier.fillMaxSize()) {
    assertSectionsSizeUnchanged(model)

    val adjustedData = adjustData(model)
    val donutProgressValues = createDonutProgressValues(adjustedData)
    animateOrSnapDistinctValues(adjustedData, config, donutProgressValues)

    DrawDonut(adjustedData, donutProgressValues, modifier)
}

@Composable private fun createDonutProgressValues(model: DonutModel): DonutProgressValues {
    val pathData = calculatePathData(model)
    val animatedGapAngle = animatedFloat(model.gapAngleDegrees)
    val animatedMasterProgress = animatedFloat(model.masterProgress)
    val animatedGapWidthDegrees = animatedFloat(model.gapWidthDegrees)
    val animatedStrokeWidth = animatedFloat(model.strokeWidth)
    val animatedBackgroundLineColor = animatedColor(model.backgroundLineColor)
    val animatedCap = animatedFloat(model.cap)
    val animatedProgressStartAngles = model.sections.mapIndexed { index, _ -> animatedFloat(pathData[index].startAngle) }
    val animatedProgressSweepAngles = model.sections.mapIndexed { index, _ -> animatedFloat(pathData[index].sweepAngle) }
    val animatedProgressColors = model.sections.map { animatedColor(it.color) }

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

@Composable private fun DrawDonut(model: DonutModel, donutProgressValues: DonutProgressValues, modifier: Modifier) {
    val masterProgress = donutProgressValues.animatedMasterProgress.value
    val gapAngleDegrees = donutProgressValues.animatedGapAngle.value
    val gapWidthDegrees = donutProgressValues.animatedGapWidthDegrees.value
    val backgroundLineColor = donutProgressValues.animatedBackgroundLineColor.value

    val wholeDonutAngle = 360f - gapWidthDegrees
    val masterSegmentAngle = wholeDonutAngle * masterProgress
    val startAngle = gapAngleDegrees + gapWidthDegrees / 2

    val masterPathData = DonutPathDataEntry(backgroundLineColor, startAngle, masterSegmentAngle)
    val entriesPathData = mutableListOf<DonutPathDataEntry>().apply {
        model.sections.forEachIndexed { index, _ ->
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

@Composable private fun assertSectionsSizeUnchanged(model: DonutModel) {
    val initialSectionsCount = remember { model.sections.size }
    check(model.sections.size <= initialSectionsCount) {
        "Adding or removing sections is not supported." +
        "Instead of adding new sections dynamically add empty sections during initialization and " +
        "instead of removing existing sections dynamically set sections value to zero."
    }
}

private fun animateOrSnapDistinctValues(model: DonutModel, config: DonutConfig, donutProgressValues: DonutProgressValues) {
    donutProgressValues.animatedGapAngle.animateOrSnapDistinctValues(
        newValue = model.gapAngleDegrees,
        isAnimationEnabled = config.isGapAngleAnimationEnabled,
        animationBuilder = config.gapAngleAnimationBuilder
    )
    donutProgressValues.animatedMasterProgress.animateOrSnapDistinctValues(
        newValue = model.masterProgress,
        isAnimationEnabled = config.isMasterProgressAnimationEnabled,
        animationBuilder = config.masterProgressAnimationBuilder
    )
    donutProgressValues.animatedGapWidthDegrees.animateOrSnapDistinctValues(
        newValue = model.gapWidthDegrees,
        isAnimationEnabled = config.isGapWidthAnimationEnabled,
        animationBuilder = config.gapWidthAnimationBuilder
    )
    donutProgressValues.animatedStrokeWidth.animateOrSnapDistinctValues(
        newValue = model.strokeWidth,
        isAnimationEnabled = config.isStrokeWidthAnimationEnabled,
        animationBuilder = config.strokeWidthAnimationBuilder
    )
    donutProgressValues.animatedStrokeWidth.animateOrSnapDistinctValues(
        newValue = model.strokeWidth,
        isAnimationEnabled = config.isStrokeWidthAnimationEnabled,
        animationBuilder = config.strokeWidthAnimationBuilder
    )
    donutProgressValues.animatedBackgroundLineColor.animateOrSnapDistinctValues(
        newValue = model.backgroundLineColor,
        isAnimationEnabled = config.isBackgroundLineColorAnimationEnabled,
        animationBuilder = config.backgroundLineColorAnimationBuilder
    )
    donutProgressValues.animatedCap.animateOrSnapDistinctValues(
        newValue = model.cap,
        isAnimationEnabled = config.isCapAnimationEnabled,
        animationBuilder = config.capAnimationBuilder
    )
    donutProgressValues.pathData.forEachIndexed { index, donutPathDataEntry ->
        donutProgressValues.animatedColors[index].animateOrSnapDistinctValues(
            newValue = donutPathDataEntry.color,
            isAnimationEnabled = config.isSectionColorAnimationEnabled,
            animationBuilder = config.sectionColorAnimationBuilder
        )
        donutProgressValues.animatedSweepAngles[index].animateOrSnapDistinctValues(
            newValue = donutPathDataEntry.sweepAngle,
            isAnimationEnabled = config.isSectionAmountAnimationEnabled,
            animationBuilder = config.sectionAmountAnimationBuilder
        )
        donutProgressValues.animatedStartAngles[index].animateOrSnapDistinctValues(
            newValue = donutPathDataEntry.startAngle,
            isAnimationEnabled = config.isSectionAmountAnimationEnabled,
            animationBuilder = config.sectionAmountAnimationBuilder
        )
    }
}

private fun adjustData(model: DonutModel) = model.copy(
    gapAngleDegrees = model.gapAngleDegrees.coerceIn(0f, 360f),
    masterProgress = model.masterProgress.coerceIn(0f, 1f),
    gapWidthDegrees = model.gapWidthDegrees.coerceIn(0f, 360f)
)

private fun calculatePathData(model: DonutModel): List<DonutPathDataEntry> {
    val allEntriesAmount = model.sectionsCap
    val wholeDonutAmount = max(model.cap, allEntriesAmount)
    val masterSegmentAmount = wholeDonutAmount * model.masterProgress
    val wholeDonutAngle = 360f - model.gapWidthDegrees
    val masterSegmentAngle = wholeDonutAngle * model.masterProgress
    val halfGap = model.gapWidthDegrees / 2
    val startAngle = model.gapAngleDegrees + halfGap
    return createPathDataForSections(
        SectionsPathData(
            startAngle = startAngle,
            masterSegmentAmount = masterSegmentAmount,
            masterSegmentAngle = masterSegmentAngle,
            masterProgress = model.masterProgress,
            sections = model.sections
        )
    )
}

private fun createPathDataForSections(data: SectionsPathData): List<DonutPathDataEntry> = with(data) {
    var angleAccumulator = startAngle
    val entriesPathData = mutableListOf<DonutPathDataEntry>()

    for (entry in sections) {
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
