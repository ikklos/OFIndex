// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json               = Json { allowStructuredMapKeys = true }
// val forumPostsResponse = json.parse(ForumPostsResponse.serializer(), jsonString)

package site.sayaz.ofindex.data.remote.response

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import site.sayaz.ofindex.data.model.Post

@Serializable
data class ForumPostsResponse (
    val count: Long,
    val message: JsonElement? = null,
    val posts: List<Post>,
    val total: Long
)

