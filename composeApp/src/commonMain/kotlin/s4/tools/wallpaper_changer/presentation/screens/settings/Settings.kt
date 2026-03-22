package s4.tools.wallpaper_changer.presentation.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import s4.tools.wallpaper_changer.domain.models.storage.AppSettings
import s4.tools.wallpaper_changer.domain.models.actions.SettingsUIAction
import s4.tools.wallpaper_changer.presentation.components.CheckBoxItem
import s4.tools.wallpaper_changer.presentation.theme.Theme

private val themeModeOptions = mapOf(
    Theme.SYSTEM to "System",
    Theme.LIGHT to "Light",
    Theme.DARK to "Dark"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(
    state: AppSettings,
    action: (SettingsUIAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
                title = {
                    Text(
                        text = "Settings"
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MultiChoiceSegmentedButtonRow {
                themeModeOptions.onEachIndexed { index, (mode, label) ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = themeModeOptions.size),
                        checked = mode == state.theme,
                        label = {
                            Text(
                                text = label
                            )
                        },
                        onCheckedChange = {
                            action(SettingsUIAction.ChangeNightMode(mode))
                        }
                    )
                }
            }
            Button(
                onClick = {
                    action(SettingsUIAction.ClearWallpapers())
                }
            ) {
                Text(
                    text = "Clear wallpapers"
                )
            }
            CheckBoxItem(
                label = "Single wallpaper",
                value = state.singleWallpaper,
                onValueChange = { action(SettingsUIAction.ToggleSingleWallpaper()) }
            )
        }
    }
}