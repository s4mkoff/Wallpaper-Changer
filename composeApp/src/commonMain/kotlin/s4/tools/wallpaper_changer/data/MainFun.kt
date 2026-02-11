package s4.tools.wallpaper_changer.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.contentLength
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.readRemaining
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import s4.tools.wallpaper_changer.changeWallpaper
import java.io.File

class MainFun(
    val workDir: String
) {

    val myScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    var showSnackbar: ((String) -> Unit)? = null

    operator fun invoke(
        link: String
    ) = myScope.launch {
        val wallpapers = searchWallpapers(link)
        println("Server response:")
        wallpapers.forEach {
            println("Wallpaper: ${it.url}")
        }
        wallpaperChanger(0, wallpapers)
    }

    private suspend fun wallpaperChanger(
        count: Int,
        wallpapers: List<WallpaperData>
    ) {
        val randomWallpaper = wallpapers.random()
        val wallpaperExtension = randomWallpaper.fileType.split('/').last()
        if (!FilesOperation.checkIsFileExist(workDir + FileNames.WALLPAPER_LIST)) {
            FilesOperation.createTxtFile(workDir + FileNames.WALLPAPER_LIST)
        }
        if (!FilesOperation.isTxtContainsText(workDir + FileNames.WALLPAPER_LIST, randomWallpaper.url)) {
            FilesOperation.appendToEndOfTxt(workDir + FileNames.WALLPAPER_LIST, randomWallpaper.url)
            downloadAndChangeWallpaper(randomWallpaper.path, wallpaperExtension)
        } else if (count < 10){
            wallpaperChanger(count + 1, wallpapers)
        } else {
            downloadAndChangeWallpaper(randomWallpaper.path, wallpaperExtension)
        }
    }

    private suspend fun downloadAndChangeWallpaper(path: String, extension: String) {
        var wallpaperName = FileNames.WALLPAPER_FILE_NAME_WITHOUT_EXTENSION + ".$extension"
        val newWallpaperName = wallpaperName.replace(".$extension", "_1.$extension")
        if (FilesOperation.checkIsFileExist(wallpaperName)) {
            FilesOperation.removeFile(wallpaperName)
            wallpaperName = newWallpaperName
        } else if (FilesOperation.checkIsFileExist(workDir + newWallpaperName)) {
            FilesOperation.removeFile(workDir + newWallpaperName)
        }
        downloadWallpaper(path, wallpaperName)
        changeWallpaper(workDir, wallpaperName)
        showSnackbar?.invoke("Success")
    }



    suspend fun downloadWallpaper(path: String, name: String) {
        val client = HttpClient()
        println("Downloading Wallpaper for $name")
        try {
            val response = client.get(path)
            if (response.status.value in 200..299) {
                val bytes = response.readRawBytes()
                val file = File("$workDir/$name")
                file.writeBytes(bytes)
                println("Downloaded wallpaper ${file.absolutePath}")
            } else {
                println("Помилка сервера: ${response.status.value}")
            }
        } catch (e: Exception) {
            println("Не вдалось завантажити файл: ${e.message}")
        }
    }

    suspend fun searchWallpapers(
        url: String
    ): List<WallpaperData> {
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

            // Отримуємо тіло відповіді та мапимо його на наш клас
            val wallhavenResponse: WallhavenResponse = response.body()
            wallhavenResponse.data
        } catch (e: Exception) {
            println("Error searching: ${e.message}")
            emptyList()
        } finally {
            client.close() // Важливо закривати клієнт, якщо ви створюєте його всередині функції
        }
    }

}