package app.futured.donutsample.ui.playground.common.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Composable
fun Circle(radius: Dp, color: Color, modifier: Modifier = Modifier) {
    Box(modifier = modifier then Modifier
        .size(radius, radius)
        .drawBehind {
            drawCircle(color, radius.toPx(), size.center)
        })
}
