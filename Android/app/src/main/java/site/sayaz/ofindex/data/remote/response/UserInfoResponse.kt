// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json             = Json { allowStructuredMapKeys = true }
// val userInfoResponse = json.parse(UserInfoResponse.serializer(), jsonString)

package site.sayaz.ofindex.data.remote.response

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

@Serializable
data class UserInfoResponse (
    val avatar: String,
    val message: JsonElement? = null,
    val userId: Long,

    val userName: String
)