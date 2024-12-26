// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json              = Json { allowStructuredMapKeys = true }
// val addCommentRequest = json.parse(AddCommentRequest.serializer(), jsonString)

package site.sayaz.ofindex.data.remote.request

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

@Serializable
data class AddCommentRequest (
    val parent: Long?,
    val postId: Long,
    val text: String
)