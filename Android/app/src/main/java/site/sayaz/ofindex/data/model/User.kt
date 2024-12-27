package site.sayaz.ofindex.data.model

import kotlinx.serialization.json.JsonElement

data class User(
    val avatar: String? = "",
    val userId: Long = -1,
    val userName: String = ""
)