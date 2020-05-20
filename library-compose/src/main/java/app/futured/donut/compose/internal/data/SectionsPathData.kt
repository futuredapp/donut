package app.futured.donut.compose.internal.data

import app.futured.donut.compose.data.DonutSection

internal data class SectionsPathData(
    val startAngle: Float,
    val masterSegmentAmount: Float,
    val masterSegmentAngle: Float,
    val masterProgress: Float,
    val sections: List<DonutSection>
)
