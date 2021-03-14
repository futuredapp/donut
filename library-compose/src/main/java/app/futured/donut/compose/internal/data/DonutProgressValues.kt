package app.futured.donut.compose.internal.data

import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color

internal data class DonutProgressValues(
    val animatedGapAngle: State<Float>,
    val animatedMasterProgress: State<Float>,
    val animatedGapWidthDegrees: State<Float>,
    val animatedStrokeWidth: State<Float>,
    val animatedBackgroundLineColor: State<Color>,
    val animatedCap: State<Float>,
    val animatedStartAngles: List<State<Float>>,
    val animatedSweepAngles: List<State<Float>>,
    val animatedColors: List<State<Color>>,
    val pathData: List<DonutPathDataEntry>
)
