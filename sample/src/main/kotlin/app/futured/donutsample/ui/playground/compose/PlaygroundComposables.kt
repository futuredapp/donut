package app.futured.donutsample.ui.playground.compose

import androidx.compose.Composable
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.Box
import androidx.ui.foundation.ContentGravity
import androidx.ui.foundation.Text
import androidx.ui.graphics.Color
import androidx.ui.layout.Column
import androidx.ui.layout.Row
import androidx.ui.layout.RowScope.gravity
import androidx.ui.layout.Spacer
import androidx.ui.layout.fillMaxSize
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.height
import androidx.ui.layout.size
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import app.futured.donut.compose.DonutProgress
import app.futured.donut.compose.data.DonutConfig
import app.futured.donut.compose.data.DonutModel
import app.futured.donut.compose.data.DonutSection
import app.futured.donutsample.ui.playground.common.compose.Circle

@Composable
fun SampleComposeScreen(model: DonutModel, config: DonutConfig) {
    Box(Modifier.fillMaxWidth() + Modifier.height(240.dp), gravity = ContentGravity.Center) {
        Box(Modifier.size(width = 240.dp, height = 240.dp)) {
            DonutProgress(model, config)
            DataOverview(model)
        }
    }
}

@Composable
fun DataOverview(model: DonutModel) {
    Box(Modifier.fillMaxSize(), gravity = ContentGravity.Center) {
        Column(modifier = Modifier.gravity(Alignment.CenterVertically)) {
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
    Text("Amount cap: ${format.format(amountCap)}", modifier = Modifier.gravity(Alignment.CenterVertically))
}

@Composable
fun TitleTotalAmount(totalAmount: Float, format: String = "%.2f") {
    Text("Total amount: ${format.format(totalAmount)}", modifier = Modifier.gravity(Alignment.CenterVertically))
}

@Composable
fun ProgressLineRowValueIndicator(section: DonutSection, format: String = "%.2f") {
    Row(modifier = Modifier.gravity(Alignment.CenterVertically)) {
        Circle(radius = 4.dp, color = section.color, modifier = Modifier.gravity(Alignment.CenterVertically))
        Spacer(modifier = Modifier.size(width = 12.dp, height = 0.dp))
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
        gapWidthDegrees = 270f,
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
    SampleComposeScreen(data, DonutConfig())
}
