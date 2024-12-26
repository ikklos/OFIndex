package site.sayaz.ofindex.data.remote.response

// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json               = Json { allowStructuredMapKeys = true }
// val addCommentResponse = json.parse(AddCommentResponse.serializer(), jsonString)


import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

@Serializable
data class AddCommentResponse (
    @SerialName("commentId")
    val commentID: Long,

    val createTime: String,
    val likes: Long,
    val text: String,
    val userAvatar: String,

    @SerialName("userId")
    val userID: Long,

    val userName: String
)