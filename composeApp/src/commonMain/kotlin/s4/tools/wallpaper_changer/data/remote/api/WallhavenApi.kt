package s4.tools.wallpaper_changer.data.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json
import s4.tools.wallpaper_changer.data.LinkBuilder
import s4.tools.wallpaper_changer.data.local.AppManagers
import s4.tools.wallpaper_changer.domain.local.storage.StorageManager
import s4.tools.wallpaper_changer.domain.models.wallpaper.WallpaperDetails
import s4.tools.wallpaper_changer.domain.models.wallpaper.wallhaven.WallhavenResponse
import s4.tools.wallpaper_changer.domain.models.wallpaper.wallhaven.WallhavenSettings
import s4.tools.wallpaper_changer.domain.remote.WallpaperApi
import s4.tools.wallpaper_changer.domain.remote.WallpaperResponse

class WallhavenApi() : WallpaperApi {

    override val apiName: String = "WallhavenApi"

    private val _settings = MutableStateFlow(WallhavenSettings())
    val settings: StateFlow<WallhavenSettings> = _settings.asStateFlow()

    init {
        loadApiSettings()
    }

    override suspend fun getWallpaperDetails(): List<WallpaperDetails> {
        TODO("Not yet implemented")
    }

    fun changeSettings(settings: WallhavenSettings) {
        _settings.update { settings }
    }

    override fun saveApiSettings() {
        val json = Json { prettyPrint = true }
        val settings = json.encodeToString(_settings.value)
        AppManagers.storageManager.saveApiSettings(
            apiName = apiName,
            apiSettings = settings
        )
    }

    override fun loadApiSettings() {
        val jsonString = AppManagers.storageManager.loadApiSettings(apiName) ?: return
        val loadedSettings = Json.Default.decodeFromString<WallhavenSettings>(jsonString)
        _settings.update { loadedSettings }
    }

    override suspend fun searchWallpapers(url: String): List<WallpaperResponse> {
        val client = HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                })
            }
        }
        return try {
            val response = client.get(url) {
                url()
            }

            println("Api call url: " + response.call.request.url.toString())
            val wallhavenResponse: WallhavenResponse = response.body()
            wallhavenResponse.data.map {
                val extension = it.fileType.split('/').last()
                WallpaperResponse(it.url, extension, it.path)
            }
        } catch (e: Exception) {
            println("Error searching: ${e.message}")
            emptyList()
        } finally {
            client.close()
        }
    }


    override fun buildLink(): String {
        return LinkBuilder("wallhaven.cc/api/v1/search").apply {
            append("categories", settings.value.categories.toValue())
            append("purity", settings.value.purity.toValue())
            append("atleast", settings.value.resolution)
            append("sorting", settings.value.sorting.value)
            append("ratios", settings.value.ratios.value)
            if (settings.value.color.isNotEmpty()) append("colors", settings.value.color)
            if (settings.value.token.isNotEmpty()) append("apikey", settings.value.token)
        }.build()
    }

}