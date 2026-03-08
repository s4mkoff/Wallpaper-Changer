package s4.tools.wallpaper_changer.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.readRawBytes
import s4.tools.wallpaper_changer.domain.remote.WallpaperNetwork
import java.io.File

class WallpaperNetworkImpl: WallpaperNetwork {

    override suspend fun downloadWallpaper(url: String, file: File) {
        val client = HttpClient()
        println("Downloading Wallpaper")
        try {
            val response = client.get(url)
            if (response.status.value in 200..299) {
                val bytes = response.readRawBytes()
                file.writeBytes(bytes)
                println("Downloaded wallpaper ${file.absolutePath}")
            } else {
                println("Помилка сервера: ${response.status.value}")
            }
        } catch (e: Exception) {
            println("Не вдалось завантажити файл: ${e.message}")
        }
    }

}