package app.futured.donutsample.ui.playground.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.futured.donut.compose.DonutProgress
import app.futured.donut.compose.data.DonutConfig
import app.futured.donut.compose.data.DonutModel
import app.futured.donut.compose.data.DonutSection
import app.futured.donutsample.ui.playground.common.compose.Circle

@Composable
fun SampleComposeScreen(model: DonutModel, config: DonutConfig) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.size(width = 240.dp, height = 240.dp)
        ) {
            DonutProgress(model, modifier = Modifier.fillMaxSize(), config = config)
            DataOverview(model)
        }
    }
}

@Composable
fun DataOverview(model: DonutModel) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            TitleAmountCap(model.sectionsCap)
            TitleTotalAmount(model.cap)
            Spacer(modifier = Modifier.size(width = 0.dp, height = 12.dp))
            model.sections.forEach { progressEntry ->
                ProgressLineRowValueIndicator(progressEntry)
            }
        }
    }
}

@Composable
fun TitleAmountCap(amountCap: Float, format: String = "%.2f") {
    Text("Amount cap: ${format.format(amountCap)}")
}

@Composable
fun TitleTotalAmount(totalAmount: Float, format: String = "%.2f") {
    Text("Total amount: ${format.format(totalAmount)}")
}

@Composable
fun ProgressLineRowValueIndicator(section: DonutSection, format: String = "%.2f") {
    Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
        Circle(radius = 4.dp, color = section.color)
        Spacer(modifier = Modifier.width(width = 12.dp))
        Text(text = format.format(section.amount))
    }
}

// Previews

@Composable
@Preview
fun SampleComposeScreenPreview() {
    val data = DonutModel(
        cap = 8f,
        masterProgress = 1f,
        gapWidthDegrees = 0f,
        gapAngleDegrees = 90f,
        strokeWidth = 40f,
        backgroundLineColor = Color.LightGray,
        sections = listOf(
            DonutSection(amount = 2.2f, color = Color.Cyan),
            DonutSection(amount = 2.2567f, color = Color.Red),
            DonutSection(amount = 2.5f, color = Color.Green),
            DonutSection(amount = 0f, color = Color.Blue)
        )
    )
    SampleComposeScreen(data, DonutConfig.create())
}
