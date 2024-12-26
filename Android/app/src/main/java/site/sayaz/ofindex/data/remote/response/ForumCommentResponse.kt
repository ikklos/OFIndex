// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json     = Json { allowStructuredMapKeys = true }
// val response = json.parse(Response.serializer(), jsonString)

package site.sayaz.ofindex.data.remote.response

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import site.sayaz.ofindex.data.model.Comment

@Serializable
data class ForumCommentResponse (
    val comments: List<Comment>,
    val count: Long,
    val postId: Long,
    val total: Long
)



