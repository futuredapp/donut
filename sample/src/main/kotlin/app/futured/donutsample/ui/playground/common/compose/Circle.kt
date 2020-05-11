package app.futured.donutsample.ui.playground.common.compose

import androidx.compose.Composable
import androidx.compose.remember
import androidx.ui.core.Modifier
import androidx.ui.core.drawBehind
import androidx.ui.foundation.Box
import androidx.ui.graphics.Color
import androidx.ui.graphics.Paint
import androidx.ui.layout.size
import androidx.ui.unit.Dp
import androidx.ui.unit.center
import androidx.ui.unit.toOffset

@Composable
fun Circle(radius: Dp, color: Color, modifier: Modifier = Modifier) {
    val paint = remember { Paint() }.apply {
        this.color = color
    }
    Box(modifier = modifier + Modifier.size(radius, radius) + Modifier.drawBehind {
        drawCircle(size.center().toOffset(), radius.toPx().value, paint)
    })
}
