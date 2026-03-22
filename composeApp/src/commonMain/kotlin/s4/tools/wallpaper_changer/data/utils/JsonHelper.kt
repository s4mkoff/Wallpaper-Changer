package s4.tools.wallpaper_changer.data.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

object JsonHelper {

    fun <T> fromJsonStringToClass(
        data: String,
        serializer: KSerializer<T>
    ): T {
        return Json.decodeFromString(serializer, data)
    }

    fun <T> fromClassToJsonString(
        data: T,
        serializer: KSerializer<T>
    ): String {
        val json = Json { prettyPrint = true }
        return json.encodeToString(serializer, data)
    }

}