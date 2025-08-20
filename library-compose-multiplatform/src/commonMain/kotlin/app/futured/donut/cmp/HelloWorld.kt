package app.futured.donut.cmp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Simple Hello World composable for testing Compose Multiplatform setup.
 * This will work on both Android and iOS.
 */
@Composable
fun HelloWorld() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Hello World from Compose Multiplatform! 🍩")
    }
}
