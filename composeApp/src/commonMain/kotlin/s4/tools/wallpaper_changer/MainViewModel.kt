package s4.tools.wallpaper_changer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import s4.tools.wallpaper_changer.data.remote.api.WallhavenApi
import s4.tools.wallpaper_changer.data.local.AppManagers
import s4.tools.wallpaper_changer.domain.models.AppSettings
import s4.tools.wallpaper_changer.domain.models.CurrentWallpaperImage
import s4.tools.wallpaper_changer.domain.models.actions.HomeUIAction
import s4.tools.wallpaper_changer.domain.models.actions.SettingsUIAction
import s4.tools.wallpaper_changer.domain.models.actions.apiSettings.WallhavenUiAction
import s4.tools.wallpaper_changer.domain.remote.WallpaperApi
import s4.tools.wallpaper_changer.domain.remote.WallpaperResponse
import s4.tools.wallpaper_changer.domain.usecase.WallpaperApiUseCases
import s4.tools.wallpaper_changer.navigation.Screens

class MainViewModel : ViewModel() {

    private var _snackbarMessage: MutableStateFlow<String> = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> = _snackbarMessage.asStateFlow()

    var currentWallpaperImage: MutableStateFlow<CurrentWallpaperImage> =
        MutableStateFlow(CurrentWallpaperImage.Loading())

    private var _appSettings: MutableStateFlow<AppSettings> = MutableStateFlow(AppSettings())
    val appSettings: StateFlow<AppSettings> = _appSettings.asStateFlow()

    var currentScreen: Screens by mutableStateOf(Screens.Home)

    var currentApi: WallpaperApi = WallhavenApi()

    val useCase = WallpaperApiUseCases(currentApi)

    init {
        loadCurrentWallpaperImage()
        loadSettings()
    }

    fun loadSettings() {
        val jsonString = AppManagers.storageManager.loadAppSettings() ?: return
        _appSettings.update {
            Json.decodeFromString<AppSettings>(jsonString)
        }
    }

    fun saveSettings() {
        val json = Json { prettyPrint = true }
        val settings = json.encodeToString(_appSettings.value)
        AppManagers.storageManager.saveSettings(settings)
    }

    fun loadCurrentWallpaperImage() {
        viewModelScope.launch {
            currentWallpaperImage.update {
                useCase.loadLastWallpaperImage()
            }
        }
    }

    private var _listOfWallpapers: MutableStateFlow<List<WallpaperResponse>?> = MutableStateFlow(null)
    val listOfWallpapers: StateFlow<List<WallpaperResponse>?> = _listOfWallpapers.asStateFlow()

    fun settingsAction(
        action: SettingsUIAction
    ) {
        when (action) {
            is SettingsUIAction.ChangeNightMode -> {
                _appSettings.update { it.copy(theme = action.theme) }
                saveSettings()
            }
            is SettingsUIAction.ClearWallpapers -> {
                viewModelScope.launch {
                    useCase.clearWallpapers()
                    _snackbarMessage.update { "Cleared" }
                }
            }
        }
    }

    fun wallhavenSettingsAction(
        action: WallhavenUiAction
    ) {
        when (action) {
            is WallhavenUiAction.ChangeSetting -> {
                viewModelScope.launch {
                    (currentApi as WallhavenApi).changeSettings(action.newSettings)
                }
            }

            is WallhavenUiAction.ChangeWallpaper -> {
                viewModelScope.launch {
                    currentWallpaperImage.update { CurrentWallpaperImage.Loading() }
                    useCase.randomWallpaper()
                    _snackbarMessage.update { "Finished" }
                    loadCurrentWallpaperImage()
                }
            }

            is WallhavenUiAction.SaveSettings -> {
                viewModelScope.launch {
                    currentApi.saveApiSettings()
                    currentScreen = Screens.Home
                    _snackbarMessage.update { "Saved" }
                }
            }

            is WallhavenUiAction.TokenError -> {
                _snackbarMessage.update { "Token empty" }
            }

            is WallhavenUiAction.Back -> {
                currentScreen = Screens.Home
            }
        }
    }

    fun wallpaperFromListClick(wallpaperResponse: WallpaperResponse) {
        viewModelScope.launch {
            currentWallpaperImage.update { CurrentWallpaperImage.Loading() }
            useCase.changeWallpaperFromApi(wallpaperResponse)
            _snackbarMessage.update { "Finished" }
            loadCurrentWallpaperImage()
            currentScreen = Screens.Home
        }
    }

    fun homeAction(action: HomeUIAction) {
        when (action) {
            is HomeUIAction.ChooseFromWallpaperList -> {
                currentScreen = Screens.WallpaperList
                viewModelScope.launch {
                    _listOfWallpapers.update { useCase.getWallpaperList() }
                }
            }

            is HomeUIAction.RandomWallpaper -> {
                viewModelScope.launch(Dispatchers.IO) {
                    currentWallpaperImage.update { CurrentWallpaperImage.Loading() }
                    useCase.randomWallpaper()
                    _snackbarMessage.update { "Finished" }
                    loadCurrentWallpaperImage()
                }
            }

            is HomeUIAction.ToApiSettings -> {
                currentScreen = Screens.ApiSettings
            }
        }
    }

}