package s4.tools.wallpaper_changer

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.painterResource
import s4.tools.wallpaper_changer.data.local.AppManagers
import s4.tools.wallpaper_changer.domain.models.AppSettings
import s4.tools.wallpaper_changer.data.remote.api.WallhavenApi
import s4.tools.wallpaper_changer.navigation.Screens
import s4.tools.wallpaper_changer.presentation.screens.wallpaperList.WallpaperList
import s4.tools.wallpaper_changer.presentation.screens.apiSettings.wallhaven.WallhavenApiSettings
import s4.tools.wallpaper_changer.presentation.screens.history.History
import s4.tools.wallpaper_changer.presentation.screens.home.HomeScreen
import s4.tools.wallpaper_changer.presentation.screens.settings.Settings
import s4.tools.wallpaper_changer.presentation.theme.Theme
import wallpaper_changer.composeapp.generated.resources.Res
import wallpaper_changer.composeapp.generated.resources.history_icon
import wallpaper_changer.composeapp.generated.resources.home_icon
import wallpaper_changer.composeapp.generated.resources.settings_icon

private fun chooseTheme(dark: Boolean): ColorScheme {
    return if (dark) darkColorScheme() else lightColorScheme()
}

@Composable
@Preview
fun App() {
    val viewModel = remember { MainViewModel() }
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(viewModel.snackbarMessage) {
        if (!viewModel.snackbarMessage.value.isEmpty()) snackbarHostState.showSnackbar(viewModel.snackbarMessage.value)
    }
    val appSettings by viewModel.appSettings.collectAsState()
    val systemDarkTheme = isSystemInDarkTheme()
    BackHandling { finish ->
        if (viewModel.currentScreen==Screens.Home) {
            finish()
        } else {
            viewModel.currentScreen = Screens.Home
        }
    }
    MaterialTheme(
        colorScheme = if (appSettings.theme==Theme.SYSTEM) chooseTheme(systemDarkTheme) else chooseTheme(appSettings.theme==Theme.DARK)
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            bottomBar = {
                Column {
                    if (viewModel.loadingState) {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                    NavigationBar(
                        modifier = Modifier
                            .fillMaxWidth(),
                        content = {
                            NavigationBarItem(
                                selected = viewModel.currentScreen == Screens.History,
                                icon = {
                                    Icon(
                                        painter = painterResource(Res.drawable.history_icon),
                                        contentDescription = "History icon"
                                    )
                                },
                                label = {
                                    Text(
                                        text = "History"
                                    )
                                },
                                onClick = {
                                    viewModel.currentScreen = Screens.History
                                }
                            )
                            NavigationBarItem(
                                selected = viewModel.currentScreen == Screens.Home,
                                icon = {
                                    Icon(
                                        painter = painterResource(Res.drawable.home_icon),
                                        contentDescription = "Home icon"
                                    )
                                },
                                label = {
                                    Text(
                                        text = "Home"
                                    )
                                },
                                onClick = {
                                    viewModel.currentScreen = Screens.Home
                                }
                            )
                            NavigationBarItem(
                                selected = viewModel.currentScreen == Screens.Settings,
                                icon = {
                                    Icon(
                                        painter = painterResource(Res.drawable.settings_icon),
                                        contentDescription = "Settings icon"
                                    )
                                },
                                label = {
                                    Text(
                                        text = "Settings"
                                    )
                                },
                                onClick = {
                                    viewModel.currentScreen = Screens.Settings
                                }
                            )
                        }
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (viewModel.currentScreen) {
                    Screens.Home -> {
                        val currentWallpaperState by viewModel.currentWallpaperImage.collectAsState()
                        HomeScreen(
                            currentApi = viewModel.currentApi.apiName,
                            currentWallpaperImage = currentWallpaperState,
                            action = { action ->
                                viewModel.homeAction(action)
                            }
                        )
                    }

                    Screens.ApiSettings -> {
                        when (viewModel.currentApi) {
                            is WallhavenApi -> {
                                val settingsState by (viewModel.currentApi as WallhavenApi).settings.collectAsState()
                                WallhavenApiSettings(
                                    state = settingsState,
                                    action = { action ->
                                        viewModel.wallhavenSettingsAction(action)
                                    }
                                )
                            }
                        }
                    }

                    Screens.WallpaperList -> {
                        val wallpaperList by viewModel.listOfWallpapers.collectAsState()
                        WallpaperList(
                            wallpapers = wallpaperList,
                            onWallpaperClick = { wallpaperResponse ->
                                viewModel.wallpaperFromListClick(wallpaperResponse)
                            }
                        )
                    }

                    Screens.Settings -> {
                        val settingsState by viewModel.appSettings.collectAsState()
                        Settings(
                            state = settingsState,
                            action = { action ->
                                viewModel.settingsAction(action)
                            }
                        )
                    }

                    Screens.History -> {
                        LaunchedEffect(Unit) {
                            viewModel.loadHistoryWallpapers()
                        }
                        val wallpapersList by viewModel.historyWallpaperList.collectAsState()
                        History(
                            wallpapersFromHistory = wallpapersList,
                            action = { action ->
                                viewModel.historyAction(action)
                            }
                        )
                    }
                }
                SnackbarHost(
                    modifier = Modifier
                        .align(Alignment.BottomCenter),
                    hostState = snackbarHostState
                )
            }
        }
    }
}