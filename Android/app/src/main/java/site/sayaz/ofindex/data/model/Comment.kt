package site.sayaz.ofindex.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Comment (
    val commentId: Long? = null,
    val comments: List<CommentComment>? = null,
    val count: Long? = null,
    val createTime: String? = null,
    val likes: Long? = null,
    val text: String? = null,
    val userAvatar: String? = null,
    val userId: Long? = null,
    val userName: String? = null
)