package site.sayaz.ofindex.util

import kotlinx.serialization.json.Json
import site.sayaz.ofindex.data.model.PackContent

fun parseNotesContent(jsonString: String): PackContent? {
    return try {
        Json.decodeFromString<PackContent>(jsonString)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}