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
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import io.ktor.http.ContentType
import kotlinx.coroutines.launch
import s4.tools.wallpaper_changer.data.MainFun
import s4.tools.wallpaper_changer.data.api.wallhaven.Ratios
import s4.tools.wallpaper_changer.data.api.wallhaven.Sorting

@Composable
@Preview
fun App() {
    val viewModel = remember { MainViewModel() }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        viewModel.loadApiParams()
    }
    MaterialTheme {
        var isTokenError by remember {
            mutableStateOf(false)
        }
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
        ) {
            LazyColumn(
                modifier = Modifier
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
                                    value = viewModel.api.categories.general,
                                    onValueChange = {
                                        viewModel.api.categories =
                                            viewModel.api.categories.copy(general = !viewModel.api.categories.general)
                                    }
                                )
                            }
                            item {
                                CheckBoxItem(
                                    label = "Anime",
                                    value = viewModel.api.categories.anime,
                                    onValueChange = {
                                        viewModel.api.categories =
                                            viewModel.api.categories.copy(anime = !viewModel.api.categories.anime)
                                    }
                                )
                            }
                            item {
                                CheckBoxItem(
                                    label = "People",
                                    value = viewModel.api.categories.people,
                                    onValueChange = {
                                        viewModel.api.categories =
                                            viewModel.api.categories.copy(people = !viewModel.api.categories.people)
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
                                    value = viewModel.api.purity.sfw,
                                    onValueChange = {
                                        viewModel.api.purity = viewModel.api.purity.copy(sfw = !viewModel.api.purity.sfw)
                                    }
                                )
                            }
                            item {
                                CheckBoxItem(
                                    label = "Sketchy",
                                    value = viewModel.api.purity.sketchy,
                                    onValueChange = {
                                        viewModel.api.purity =
                                            viewModel.api.purity.copy(sketchy = !viewModel.api.purity.sketchy)
                                    }
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
                                            viewModel.api.purity =
                                                viewModel.api.purity.copy(nsfw = !viewModel.api.purity.nsfw)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
//                if (getPlatform().name.contains("Java", true)) {
//                    item {
//                        Column {
//                            Text(
//                                text = "Folder to download in"
//                            )
//                        }
//                    }
//                }
                item {
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
                                    value = item == viewModel.api.sorting,
                                    onValueChange = { viewModel.api.sorting = item }
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
                                    value = item == viewModel.api.ratios,
                                    onValueChange = { viewModel.api.ratios = item }
                                )
                            }
                        }
                    }
                }
//                item {
//                    Column {
//                        Text(
//                            text = "Color"
//                        )
//                        Box(
//                            modifier = Modifier
//                                .size(100.dp)
//                                .background(if viewModel.api.color)
//                        )
//                    }
//                }
                item {
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
                }
                item {
                    Row {
                        Button(
                            onClick = {
                                viewModel.changeWallpaper(
                                    showSnackbar = { message ->
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar(message)
                                        }
                                    }
                                )
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
                if (getPlatform().name.contains("Android")) {
                    item {
                        Row {
                            Button(
                                onClick = {
                                    scheduleWorkManager()
                                }
                            ) {
                                Text(
                                    text = "Schedule 15 min"
                                )
                            }
                            Button(
                                onClick = {
                                    cancelWorkManager()
                                }
                            ) {
                                Text(
                                    text = "Cancel scheduling"
                                )
                            }
                        }
                    }
                    item {
                        var workManagerStatus by remember {
                            mutableStateOf("Loading status")
                        }
                        LaunchedEffect(
                            Unit
                        ) {
                            observeWorkManagerState(
                                lifecycleOwner = lifecycleOwner,
                                observerStatus = { status -> workManagerStatus = status }
                            )
                        }
                        Text(
                            text = workManagerStatus
                        )
                    }
                    item {
                        Button(
                            onClick = {
                                    exportFilesInExternal { result ->
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar(result)
                                        }
                                    }

                            }
                        ) {
                            Text(
                                text = "Експортувати"
                            )
                        }
                    }
                }
            }
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            )
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