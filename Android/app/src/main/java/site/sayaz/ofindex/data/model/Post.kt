package site.sayaz.ofindex.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class Post (
    val bookId: Long? = null,
    val createTime: String? = null,
    val images: List<String> = emptyList(),
    val likes: Long? = null,
    val message: String? = null,
    val packId: Long? = null,
    val posterAvatar: String? = null,
    val posterId: Long? = null,
    val posterName: String? = null,
    val postId: Long? = null,
    val tags: List<String> = emptyList(),
    val title: String? = null,
    val description:String? = null,
    val text:String? = null
)
