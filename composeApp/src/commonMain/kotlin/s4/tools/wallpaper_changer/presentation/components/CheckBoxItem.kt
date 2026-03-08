package s4.tools.wallpaper_changer.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable


@Composable
fun CheckBoxItem(
    label: String,
    value: Boolean,
    onValueChange: () -> Unit
) {
    Column {
        Text(
            text = label
        )
        Checkbox(
            checked = value,
            onCheckedChange = { onValueChange() }
        )
    }
}