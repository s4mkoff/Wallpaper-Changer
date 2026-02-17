package s4.tools.wallpaper_changer.worker

import android.content.Context
import android.provider.MediaStore
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay
import s4.tools.wallpaper_changer.data.FileNames
import s4.tools.wallpaper_changer.data.FilesOperation
import s4.tools.wallpaper_changer.data.MainFun
import s4.tools.wallpaper_changer.data.api.wallhaven.WallhavenApi
import s4.tools.wallpaper_changer.data.storage.WallpaperStorage
import s4.tools.wallpaper_changer.getFilesDirectory
import java.sql.Time
import java.sql.Timestamp
import java.time.LocalDateTime

const val WORKER_ERROR = "worker_error.txt"

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
            if (!FilesOperation.checkIsFileExist(workDir + WORKER_ERROR)) FilesOperation.createTxtFile(workDir + WORKER_ERROR)
            val timeMillis = System.currentTimeMillis()
            val formatted = java.text.SimpleDateFormat(
                "dd MMMM, HH:mm",
                java.util.Locale("uk", "UA")
            ).format(java.util.Date(timeMillis))
            FilesOperation.appendToEndOfTxt(
                workDir + WORKER_ERROR, "$formatted + ${e.message}"
            )
            Result.retry()
        }
    }

    companion object {
        const val WORKER_NAME = "wallpaper_worker"
    }
}
