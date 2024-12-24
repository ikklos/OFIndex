package site.sayaz.ofindex.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class Post (
    val bookId: Long? = null,
    val createTime: String? = null,
    val images: JsonElement? = null,
    val likes: Long? = null,
    val message: JsonElement? = null,
    val packId: Long? = null,
    val posterAvatar: String? = null,
    val posterId: Long? = null,
    val posterName: String? = null,
    val postId: Long? = null,
    val tags: List<String>? = null,
    val title: String? = null
)