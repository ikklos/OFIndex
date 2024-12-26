// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json           = Json { allowStructuredMapKeys = true }
// val addPostRequest = json.parse(AddPostRequest.serializer(), jsonString)

package site.sayaz.ofindex.data.remote.request

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

@Serializable
data class AddPostRequest (
    val picture: List<String>,
    val tags: List<String>,
    val text: String,
    val title: String
)