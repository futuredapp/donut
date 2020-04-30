package app.futured.donut.library.compose.internal.data

import app.futured.donut.library.compose.data.DonutDataset

internal data class DatasetsPathData(
    val startAngle: Float,
    val masterSegmentAmount: Float,
    val masterSegmentAngle: Float,
    val masterProgress: Float,
    val datasets: List<DonutDataset>
)
