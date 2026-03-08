package s4.tools.wallpaper_changer.presentation.screens.settings

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AppBarRow
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import s4.tools.wallpaper_changer.domain.models.AppSettings
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
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            item {
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
            }
            item {
                Button(
                    onClick = {
                        action(SettingsUIAction.ClearWallpapers())
                    }
                ) {
                    Text(
                        text = "Clear wallpapers"
                    )
                }
            }
        }
    }
}