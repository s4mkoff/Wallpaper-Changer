package s4.tools.wallpaper_changer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import s4.tools.wallpaper_changer.data.api.wallhaven.Ratios
import s4.tools.wallpaper_changer.data.api.wallhaven.Sorting

@Composable
@Preview
fun App(
) {
    val viewModel = remember { MainViewModel() }
    LaunchedEffect(Unit) {
        viewModel.loadApiParams()
    }
    MaterialTheme {
        var isTokenError by remember {
            mutableStateOf(false)
        }
        LazyColumn(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column {
                        Text(
                            text = "Categories"
                        )
                        LazyRow {
                            item {
                                CheckBoxItem(
                                    label = "General",
                                    value = viewModel.api.categories.general,
                                    onValueChange = { viewModel.api.categories = viewModel.api.categories.copy( general = !viewModel.api.categories.general) }
                                )
                            }
                            item {
                                CheckBoxItem(
                                    label = "Anime",
                                    value = viewModel.api.categories.anime,
                                    onValueChange = { viewModel.api.categories = viewModel.api.categories.copy( anime = !viewModel.api.categories.anime) }
                                )
                            }
                            item {
                                CheckBoxItem(
                                    label = "People",
                                    value = viewModel.api.categories.people,
                                    onValueChange = { viewModel.api.categories = viewModel.api.categories.copy( people = !viewModel.api.categories.people) }
                                )
                            }
                        }
                    }
                    Column {
                        Text(
                            text = "Purity"
                        )
                        LazyRow {
                            item {
                                CheckBoxItem(
                                    label = "SWF",
                                    value = viewModel.api.purity.sfw,
                                    onValueChange = { viewModel.api.purity = viewModel.api.purity.copy( sfw = !viewModel.api.purity.sfw) }
                                )
                            }
                            item {
                                CheckBoxItem(
                                    label = "Sketchy",
                                    value = viewModel.api.purity.sketchy,
                                    onValueChange = { viewModel.api.purity = viewModel.api.purity.copy( sketchy = !viewModel.api.purity.sketchy) }
                                )
                            }
                            item {
                                CheckBoxItem(
                                    label = "NSFW",
                                    value = viewModel.api.purity.nsfw,
                                    onValueChange = {
                                        if (viewModel.api.token.isEmpty()) {
                                            isTokenError = true
                                        } else {
                                            viewModel.api.purity = viewModel.api.purity.copy( nsfw = !viewModel.api.purity.nsfw)
                                        }
                                    }
                                )
                            }
                        }
                    }
                    Column {
                        Text(
                            text = "Resolution"
                        )
                        TextField(
                            value = viewModel.api.resolution,
                            onValueChange = { resolution ->
                                viewModel.api.resolution = resolution
                            }
                        )
                    }
                    Column {
                        Text(
                            text = "Sorting"
                        )
                        LazyRow {
                            items(Sorting.entries) { item ->
                                CheckBoxItem(
                                    label = item.name,
                                    value = item == viewModel.api.sorting,
                                    onValueChange = { viewModel.api.sorting = item }
                                )
                            }
                        }
                    }
                    Column {
                        Text(
                            text = "Ratios"
                        )
                        LazyRow {
                            items(Ratios.entries) { item ->
                                CheckBoxItem(
                                    label = item.value,
                                    value = item == viewModel.api.ratios,
                                    onValueChange = { viewModel.api.ratios = item }
                                )
                            }
                        }
                    }
                    Column {
                        Text(
                            text = "Token"
                        )
                        TextField(
                            value = viewModel.api.token,
                            isError = isTokenError,
                            onValueChange = { token ->
                                isTokenError = false
                                viewModel.api.token = token
                            }
                        )
                    }
                    Row {
                        Button(
                            onClick = {
                                viewModel.changeWallpaper()
                            }
                        ) {
                            Text(
                                text = "Change wallpaper"
                            )
                        }
                        Button(
                            onClick = {
                                viewModel.saveApiParams()
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
}

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