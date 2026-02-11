package s4.tools.wallpaper_changer.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay
import s4.tools.wallpaper_changer.data.MainFun
import s4.tools.wallpaper_changer.data.api.wallhaven.WallhavenApi
import s4.tools.wallpaper_changer.data.storage.WallpaperStorage
import s4.tools.wallpaper_changer.getFilesDirectory

class WallpaperWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    val workDir = getFilesDirectory()

    override suspend fun doWork(): Result {
        return try {
            println("Worker started")
            val api = WallhavenApi()
            WallpaperStorage.loadApiParams(workDir) {
                settings -> api.applySettings(settings)
            }
            MainFun(workDir).invoke(api.buildLink()).join()
            println("Worker ended")
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        const val WORKER_NAME = "wallpaper_worker"
    }
}
