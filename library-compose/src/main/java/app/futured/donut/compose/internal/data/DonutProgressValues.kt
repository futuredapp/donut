package app.futured.donut.compose.internal.data

import androidx.animation.AnimatedFloat
import app.futured.donut.compose.internal.AnimatedColor

internal data class DonutProgressValues(
    var animatedGapAngle: AnimatedFloat,
    var animatedMasterProgress: AnimatedFloat,
    var animatedGapWidthDegrees: AnimatedFloat,
    var animatedStrokeWidth: AnimatedFloat,
    var animatedBackgroundLineColor: AnimatedColor,
    var animatedCap: AnimatedFloat,
    val animatedStartAngles: List<AnimatedFloat>,
    val animatedSweepAngles: List<AnimatedFloat>,
    val animatedColors: List<AnimatedColor>,
    val pathData: List<DonutPathDataEntry>
)
