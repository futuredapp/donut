package app.futured.donut.sample.cmp.playground

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.futured.donut.compose.DonutProgress
import app.futured.donut.compose.data.DonutConfig
import app.futured.donut.compose.data.DonutModel
import app.futured.donut.compose.data.DonutSection
import app.futured.donut.sample.cmp.playground.tools.Circle
import app.futured.donut.sample.cmp.playground.tools.formatDecimals


@Composable
fun SampleDonutComponent(
    modifier: Modifier = Modifier,
    model: DonutModel,
    config: DonutConfig,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier.size(width = 240.dp, height = 240.dp),
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
fun TitleAmountCap(amountCap: Float, decimalPoints: Int = 2) {
    Text("Amount cap: ${amountCap.formatDecimals(decimalPoints)}")
}

@Composable
fun TitleTotalAmount(totalAmount: Float, decimalPoints: Int = 2) {
    Text("Total amount: ${totalAmount.formatDecimals(decimalPoints)}")
}

@Composable
fun ProgressLineRowValueIndicator(section: DonutSection, decimalPoints: Int = 2) {
    Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
        Circle(radius = 4.dp, color = section.color)
        Spacer(modifier = Modifier.width(width = 12.dp))
        Text(text = section.amount.formatDecimals(decimalPoints))
    }
}
