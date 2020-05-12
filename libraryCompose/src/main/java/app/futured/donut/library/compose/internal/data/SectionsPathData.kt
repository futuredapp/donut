package app.futured.donut.library.compose.internal.data

import app.futured.donut.library.compose.data.DonutSection

internal data class SectionsPathData(
    val startAngle: Float,
    val masterSegmentAmount: Float,
    val masterSegmentAngle: Float,
    val masterProgress: Float,
    val sections: List<DonutSection>
)
