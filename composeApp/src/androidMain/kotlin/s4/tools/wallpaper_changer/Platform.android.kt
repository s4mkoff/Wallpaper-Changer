package s4.tools.wallpaper_changer

import android.Manifest
import android.app.WallpaperManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import s4.tools.wallpaper_changer.worker.WallpaperWorker
import java.io.File
import java.util.concurrent.TimeUnit

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

object AppContext {
    lateinit var appContext: Context
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun changeWallpaper(dir: String, wallpaperName: String) {
    val file = File(dir + wallpaperName)
    val context = AppContext.appContext
    val wallpaperManager = WallpaperManager.getInstance(context)
    wallpaperManager.setStream(file.inputStream())
}

actual fun getFilesDirectory(): String {
    val directory = AppContext.appContext.filesDir.absolutePath
    println("directory: $directory")
    return "$directory/"
}

actual fun cancelWorkManager() {
    WorkManager.getInstance(AppContext.appContext)
        .cancelUniqueWork(WallpaperWorker.WORKER_NAME)
}

actual fun scheduleWorkManager() {
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED) // WiFi
        .setRequiresBatteryNotLow(true)
        .build()

    val request = PeriodicWorkRequestBuilder<WallpaperWorker>(
        15, TimeUnit.MINUTES
    )
        .setConstraints(constraints)
        .build()

    WorkManager.getInstance(AppContext.appContext).enqueueUniquePeriodicWork(
        WallpaperWorker.WORKER_NAME,
        ExistingPeriodicWorkPolicy.UPDATE,
        request
    )
}