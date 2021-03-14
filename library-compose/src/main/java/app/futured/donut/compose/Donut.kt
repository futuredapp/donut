package app.futured.donut.compose

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.futured.donut.compose.data.DonutConfig
import app.futured.donut.compose.data.DonutModel
import app.futured.donut.compose.data.DonutSection
import app.futured.donut.compose.internal.data.DonutPathData
import app.futured.donut.compose.internal.data.DonutPathDataEntry
import app.futured.donut.compose.internal.data.DonutProgressValues
import app.futured.donut.compose.internal.data.SectionsPathData
import kotlin.math.max

/**
 * [DonutProgress] is a composable Android view that helps you to easily create doughnut-like charts with fully
 * customizable animations.
 *
 * @param model data used to draw the content of the Donut
 * @param config configuration used to define animations setup of the Donut
 */
@Composable
fun DonutProgress(model: DonutModel, modifier: Modifier = Modifier, config: DonutConfig = DonutConfig.create()) {
    assertSectionsSizeUnchanged(model)

    val adjustedData = adjustData(model)
    val donutProgressValues = createDonutProgressValues(adjustedData, config)
    DrawDonut(adjustedData, donutProgressValues, modifier)
}

@Composable
private fun createDonutProgressValues(model: DonutModel, donutConfig: DonutConfig): DonutProgressValues {
    val pathData = calculatePathData(model)

    val animatedGapAngle = animateFloatAsState(
        targetValue = model.gapAngleDegrees,
        animationSpec = donutConfig.gapAngleAnimationSpec
    )
    val animatedMasterProgress = animateFloatAsState(
        targetValue = model.masterProgress,
        animationSpec = donutConfig.masterProgressAnimationSpec
    )
    val animatedGapWidthDegrees = animateFloatAsState(
        targetValue = model.gapWidthDegrees,
        animationSpec = donutConfig.gapWidthAnimationSpec
    )
    val animatedStrokeWidth = animateFloatAsState(
        targetValue = model.strokeWidth,
        animationSpec = donutConfig.strokeWidthAnimationSpec
    )
    val animatedBackgroundLineColor = animateColorAsState(
        targetValue = model.backgroundLineColor,
        animationSpec = donutConfig.backgroundLineColorAnimationSpec
    )
    val animatedCap = animateFloatAsState(targetValue = model.cap, animationSpec = donutConfig.capAnimationSpec)
    val animatedProgressStartAngles = model.sections.mapIndexed { index, _ ->
        animateFloatAsState(
            targetValue = pathData[index].startAngle,
            animationSpec = donutConfig.sectionAmountAnimationSpec
        )
    }
    val animatedProgressSweepAngles = model.sections.mapIndexed { index, _ ->
        animateFloatAsState(
            targetValue = pathData[index].sweepAngle,
            animationSpec = donutConfig.sectionAmountAnimationSpec
        )
    }
    val animatedProgressColors = model.sections.map {
        animateColorAsState(
            targetValue = it.color,
            animationSpec = donutConfig.sectionColorAnimationSpec
        )
    }

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

@Composable
private fun DrawDonut(model: DonutModel, donutProgressValues: DonutProgressValues, modifier: Modifier) {
    val masterProgress = donutProgressValues.animatedMasterProgress.value
    val gapAngleDegrees = donutProgressValues.animatedGapAngle.value
    val gapWidthDegrees = donutProgressValues.animatedGapWidthDegrees.value
    val backgroundLineColor = donutProgressValues.animatedBackgroundLineColor.value

    val wholeDonutAngle = 360f - gapWidthDegrees
    val masterSegmentAngle = wholeDonutAngle * masterProgress
    val startAngle = gapAngleDegrees + gapWidthDegrees / 2
    val strokeCap = StrokeCap.Round // TODO StrokeCap feature

    val masterPathData = DonutPathDataEntry(
        color = backgroundLineColor,
        startAngle = startAngle,
        sweepAngle = masterSegmentAngle
    )
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

    Canvas(modifier = modifier, onDraw = {
        drawDonutSegment(donutProgressValues.animatedStrokeWidth.value, strokeCap, donutPathData.masterPathData)
        donutPathData.entriesPathData.forEach { pathData ->
            drawDonutSegment(donutProgressValues.animatedStrokeWidth.value, strokeCap, pathData)
        }
    })
}

// TODO remove this limitation
@Composable
private fun assertSectionsSizeUnchanged(model: DonutModel) {
    val initialSectionsCount = remember { model.sections.size }
    check(model.sections.size <= initialSectionsCount) {
        "Adding or removing sections is not supported." +
                "Instead of adding new sections dynamically add empty sections during initialization and " +
                "instead of removing existing sections dynamically set sections value to zero."
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

private fun DrawScope.drawDonutSegment(
    strokeWidth: Float,
    strokeCap: StrokeCap,
    data: DonutPathDataEntry
) {
    drawArc(
        color = data.color,
        startAngle = data.startAngle,
        sweepAngle = data.sweepAngle,
        useCenter = false,
        topLeft = Offset.Zero + Offset(
            x = strokeWidth / 2f,
            y = strokeWidth / 2f
        ),
        size = Size(
            size.width - strokeWidth,
            size.height - strokeWidth
        ),
        style = Stroke(
            width = strokeWidth,
            cap = strokeCap
        )
    )
}
