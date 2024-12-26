package site.sayaz.ofindex.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CommentComment (
    val commentId: Long? = null,
    val createTime: String? = null,
    val likes: Long? = null,
    val text: String? = null,
    val userAvatar: String? = null,
    val userId: Long? = null,
    val userName: String? = null
)