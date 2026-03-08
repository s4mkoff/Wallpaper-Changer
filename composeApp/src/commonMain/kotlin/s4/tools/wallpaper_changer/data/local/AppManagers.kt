package s4.tools.wallpaper_changer.data.local

import androidx.compose.runtime.mutableStateOf
import s4.tools.wallpaper_changer.data.local.storage.StorageManagerImpl
import s4.tools.wallpaper_changer.data.remote.WallpaperNetworkImpl
import s4.tools.wallpaper_changer.getFilesManager
import s4.tools.wallpaper_changer.getWallpaperChanger
import s4.tools.wallpaper_changer.domain.local.os.FilesManager
import s4.tools.wallpaper_changer.domain.local.os.WallpaperChanger
import s4.tools.wallpaper_changer.domain.local.storage.StorageManager
import s4.tools.wallpaper_changer.domain.remote.WallpaperNetwork

object AppManagers {

    val filesManager: FilesManager by lazy { getFilesManager() }
    val storageManager: StorageManager by lazy { StorageManagerImpl(filesManager) }

    val wallpaperChanger: WallpaperChanger by lazy { getWallpaperChanger() }

    val wallpaperNetwork: WallpaperNetwork by lazy { WallpaperNetworkImpl() }

}