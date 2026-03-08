package s4.tools.wallpaper_changer.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

const val WORKER_ERROR = "worker_error.txt"

class WallpaperWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
//    val workDir = getFilesDirectory()

    override suspend fun doWork(): Result {
        return try {
//            println("Worker started")
//            val api = WallhavenApi()
//            WallpaperStorage.loadApiParams(workDir) {
//                settings -> api.applySettings(settings)
//            }
//            MainFun(workDir).invoke(api.buildLink()).join()
//            println("Worker ended")
            Result.success()
        } catch (e: Exception) {
//            if (!FilesOperation.checkIsFileExist(workDir + WORKER_ERROR)) FilesOperation.createTxtFile(workDir + WORKER_ERROR)
//            val timeMillis = System.currentTimeMillis()
//            val formatted = java.text.SimpleDateFormat(
//                "dd MMMM, HH:mm",
//                java.util.Locale("uk", "UA")
//            ).format(java.util.Date(timeMillis))
//            FilesOperation.appendToEndOfTxt(
//                workDir + WORKER_ERROR, "$formatted + ${e.message}"
//            )
            Result.retry()
        }
    }

    companion object {
        const val WORKER_NAME = "wallpaper_worker"
    }
}
