// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json                = Json { allowStructuredMapKeys = true }
// val forumDetailResponse = json.parse(ForumDetailResponse.serializer(), jsonString)

package site.sayaz.ofindex.data.remote.response

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

@Serializable
data class ForumDetailResponse (
    val bookId: Long,
    val createTime: String,
    val images: List<String> = emptyList(),
    val likes: Long,
    val message: String,
    val packId: Long,
    val posterAvatar: String,
    val posterId: Long,
    val postId: Long,
    val tags: List<String>,
    val title: String
)