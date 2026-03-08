package s4.tools.wallpaper_changer.presentation.screens.apiSettings.wallhaven

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import s4.tools.wallpaper_changer.domain.models.wallpaper.wallhaven.remoteModels.Ratios
import s4.tools.wallpaper_changer.domain.models.wallpaper.wallhaven.remoteModels.Sorting
import s4.tools.wallpaper_changer.domain.models.wallpaper.wallhaven.WallhavenSettings
import s4.tools.wallpaper_changer.domain.models.actions.apiSettings.WallhavenUiAction
import s4.tools.wallpaper_changer.presentation.components.CheckBoxItem
import wallpaper_changer.composeapp.generated.resources.Res
import wallpaper_changer.composeapp.generated.resources.back_icon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WallhavenApiSettings(
    state: WallhavenSettings,
    action: (WallhavenUiAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Wallhaven Api Settings"
                    )
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .clickable(
                                onClick = {
                                    action(WallhavenUiAction.Back())
                                }
                            ),
                        painter = painterResource(Res.drawable.back_icon),
                        contentDescription = "Back icon"
                    )
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column {
                    Text(
                        text = "Categories"
                    )
                    LazyRow {
                        item {
                            CheckBoxItem(
                                label = "General",
                                value = state.categories.general,
                                onValueChange = {
                                    action(
                                        WallhavenUiAction.ChangeSetting(
                                            state.copy(
                                                categories = state.categories.copy(
                                                    general = !state.categories.general
                                                )
                                            )
                                        )

                                    )
                                }
                            )
                        }
                        item {
                            CheckBoxItem(
                                label = "Anime",
                                value = state.categories.anime,
                                onValueChange = {
                                    action(
                                        WallhavenUiAction.ChangeSetting(
                                            state.copy(
                                                categories = state.categories.copy(
                                                    anime = !state.categories.anime
                                                )
                                            )
                                        )
                                    )
                                }
                            )
                        }
                        item {
                            CheckBoxItem(
                                label = "People",
                                value = state.categories.people,
                                onValueChange = {
                                    action(
                                        WallhavenUiAction.ChangeSetting(
                                            state.copy(
                                                categories = state.categories.copy(
                                                    people = !state.categories.people
                                                )
                                            )
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
            item {
                Column {
                    Text(
                        text = "Purity"
                    )
                    LazyRow {
                        item {
                            CheckBoxItem(
                                label = "SWF",
                                value = state.purity.sfw,
                                onValueChange = {
                                    action(
                                        WallhavenUiAction.ChangeSetting(
                                            state.copy(
                                                purity = state.purity.copy(
                                                    sfw = !state.purity.sfw
                                                )
                                            )
                                        )
                                    )
                                }
                            )
                        }
                        item {
                            CheckBoxItem(
                                label = "Sketchy",
                                value = state.purity.sketchy,
                                onValueChange = {
                                    action(
                                        WallhavenUiAction.ChangeSetting(
                                            state.copy(
                                                purity = state.purity.copy(
                                                    sketchy = !state.purity.sketchy
                                                )
                                            )
                                        )
                                    )
                                }
                            )
                        }
                        item {
                            CheckBoxItem(
                                label = "NSFW",
                                value = state.purity.nsfw,
                                onValueChange = {
                                    if (state.token.isEmpty()) {
                                        action(WallhavenUiAction.TokenError())
                                    } else {
                                        action(
                                            WallhavenUiAction.ChangeSetting(
                                                state.copy(
                                                    purity = state.purity.copy(
                                                        nsfw = !state.purity.nsfw
                                                    )
                                                )
                                            )
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
            item {
                Column {
                    Text(
                        text = "Resolution"
                    )
                    TextField(
                        value = state.resolution,
                        onValueChange = { resolution ->
                            action(
                                WallhavenUiAction.ChangeSetting(
                                    state.copy(
                                        resolution = resolution
                                    )
                                )
                            )
                        }
                    )
                }
            }
            item {
                Column {
                    Text(
                        text = "Sorting"
                    )
                    LazyRow {
                        items(Sorting.entries) { item ->
                            CheckBoxItem(
                                label = item.name,
                                value = item == state.sorting,
                                onValueChange = {
                                    action(
                                        WallhavenUiAction.ChangeSetting(
                                            state.copy(
                                                sorting = item
                                            )
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
            item {
                Column {
                    Text(
                        text = "Ratios"
                    )
                    LazyRow {
                        items(Ratios.entries) { item ->
                            CheckBoxItem(
                                label = item.value,
                                value = item == state.ratios,
                                onValueChange = {
                                    action(
                                        WallhavenUiAction.ChangeSetting(
                                            state.copy(
                                                ratios = item
                                            )
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
            item {
                Column {
                    Text(
                        text = "Token"
                    )
                    TextField(
                        value = state.token,
                        onValueChange = { token ->
                            action(
                                WallhavenUiAction.ChangeSetting(
                                    state.copy(
                                        token = token
                                    )
                                )
                            )
                        }
                    )
                }
            }
            item {
                Row {
                    Button(
                        onClick = {
                            action(
                                WallhavenUiAction.SaveSettings()
                            )
                        }
                    ) {
                        Text(
                            text = "Save params"
                        )
                    }
                }
            }
        }
    }

}