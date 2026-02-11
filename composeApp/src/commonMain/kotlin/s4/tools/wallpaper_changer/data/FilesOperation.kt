package s4.tools.wallpaper_changer.data

import java.awt.SystemColor.text
import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.exists

object FilesOperation {

    fun checkIsFileExist(fileName: String): Boolean {
        return Path(fileName).exists()
    }

    fun appendToEndOfTxt(fileName: String, text: String) {
        File(fileName).appendText(text + "\n")
    }

    fun isTxtContainsText(fileName: String, text: String): Boolean {
        val file = File(fileName)
        return if (file.exists()) file.readText().contains(text) else false
    }

    fun createTxtFile(fileName: String) {
        File(fileName).createNewFile()
    }

    fun removeFile(fileName: String) {
        File(fileName).delete()
    }

}