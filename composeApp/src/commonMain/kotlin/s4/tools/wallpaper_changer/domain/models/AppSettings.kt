package s4.tools.wallpaper_changer.domain.models

import kotlinx.serialization.Serializable
import s4.tools.wallpaper_changer.presentation.theme.Theme

@Serializable
data class AppSettings(
    var theme: Theme = Theme.SYSTEM
)