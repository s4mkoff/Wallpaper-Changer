package s4.tools.wallpaper_changer

import android.app.WallpaperManager
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.LifecycleOwner
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import s4.tools.wallpaper_changer.worker.WallpaperWorker
import java.io.File
import java.io.FileInputStream
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
    try {
        WorkManager.getInstance(AppContext.appContext)
            .cancelUniqueWork(WallpaperWorker.WORKER_NAME)
        println("Scheduler cancel success")
    } catch (e: Exception) {
        println("Scheduler cancel exception: ${e.message}")
    }
}

actual fun observeWorkManagerState(
    lifecycleOwner: LifecycleOwner,
    observerStatus: (String) -> Unit
) {
    val workManager = WorkManager.getInstance(AppContext.appContext)

    workManager.getWorkInfosForUniqueWorkLiveData(WallpaperWorker.WORKER_NAME)
        .observe(lifecycleOwner) { workInfos ->
            if (workInfos.isNullOrEmpty()) {
                observerStatus("Статус: Не заплановано")
            } else {
                val workInfo = workInfos.last()
                when (workInfo.state) {
                    WorkInfo.State.ENQUEUED -> observerStatus("Статус: В черзі (Заплановано)")
                    WorkInfo.State.RUNNING -> observerStatus("Статус: Виконується")
                    WorkInfo.State.SUCCEEDED -> observerStatus("Статус: Завершено")
                    else -> observerStatus("Статус: Скасовано або помилка: ${workInfo.state.name}")
                }
            }
        }
}

actual fun scheduleWorkManager() {
//    val constraints = Constraints.Builder()
//        .build()

    try {
        val request = PeriodicWorkRequestBuilder<WallpaperWorker>(
            15, TimeUnit.MINUTES
        )
//        .setConstraints(constraints)
            .build()

        WorkManager.getInstance(AppContext.appContext).enqueueUniquePeriodicWork(
            WallpaperWorker.WORKER_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
        println("Scheduler success")
    } catch (e: Exception) {
        println("Scheduler exception: ${e.message}")
    }
}

actual fun exportFilesInExternal(callback: (String) -> Unit) {
        val context = AppContext.appContext
        val internalDir = File(context.filesDir.absolutePath) // Ваша внутрішня папка

        if (!internalDir.exists() || !internalDir.isDirectory) {
            callback("Внутрішня папка порожня або не існує")
            return
        }

        val files = internalDir.listFiles()?.filter { it.isFile }
        if (files.isNullOrEmpty()) {
            callback("Файлів для експорту не знайдено")
            return
        }

        val resolver = context.contentResolver

        files.forEach { file ->
            try {
                // Налаштовуємо метадані файлу для MediaStore
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, file.name)
                    put(MediaStore.MediaColumns.MIME_TYPE, getMimeType(file))
                    // Вказуємо шлях: Documents/WallpaperChanger
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        put(MediaStore.MediaColumns.RELATIVE_PATH, "Documents/WallpaperChanger")
                        put(MediaStore.MediaColumns.IS_PENDING, 1)
                    }
                }

                // Вибираємо колекцію (Documents)
                val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
                } else {
                    MediaStore.Files.getContentUri("external")
                }

                val uri = resolver.insert(collection, contentValues)

                uri?.let { targetUri ->
                    resolver.openOutputStream(targetUri)?.use { outputStream ->
                        FileInputStream(file).use { inputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        contentValues.clear()
                        contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                        resolver.update(targetUri, contentValues, null, null)
                    }
                    callback("Файл ${file.name} експортовано у Documents/WallpaperChanger")
                }
            } catch (e: Exception) {
                callback("Помилка при експортуванні ${file.name}: ${e.message}")
            }
        }
        callback("Експорт завершено успішно")
}

    // Допоміжна функція для визначення типу файлу
 fun getMimeType(file: File): String {
        return when (file.extension.lowercase()) {
            "json" -> "application/json"
            "jpeg", "jpg" -> "image/jpeg"
            "png" -> "image/png"
            else -> "application/octet-stream"
        }
    }