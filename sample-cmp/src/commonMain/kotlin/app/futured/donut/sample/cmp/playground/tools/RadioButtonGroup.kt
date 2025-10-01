package app.futured.donut.sample.cmp.playground.tools

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun <T> RadioButtonGroup(
    modifier: Modifier = Modifier,
    groupTitle: String,
    radioOptions: List<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit,
    getDisplayText: (T) -> String,
    getDisplayTextResource: @Composable ((T) -> String)? = null,
) {
    val currentDisplayText = remember { getDisplayText }
    val currentDisplayTextResource = remember { getDisplayTextResource }

    Text(
        text = groupTitle,
    )

    Row(modifier.selectableGroup().horizontalScroll(rememberScrollState())) {
        radioOptions.forEach { option ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = (option == selectedOption),
                        onClick = { onOptionSelected(option) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp)
                ,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (option == selectedOption),
                    onClick = null,
                )
                Text(
                    text = currentDisplayTextResource?.invoke(option) ?: currentDisplayText(option),
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}
