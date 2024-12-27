package site.sayaz.ofindex.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pack (
    val authorAvatar: String? = " ",
    val authorId: Long? = -1,
    val name: String? = " ",
    val packId: Long? = -1,
    val description: String? = " ",
    val likes: Long? = 0,
    val liked: Boolean? = false,
    val content: String? = "",
)

