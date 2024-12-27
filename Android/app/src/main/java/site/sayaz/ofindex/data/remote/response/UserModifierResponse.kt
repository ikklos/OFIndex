package site.sayaz.ofindex.data.remote.response

// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json                 = Json { allowStructuredMapKeys = true }
// val userModifierResponse = json.parse(UserModifierResponse.serializer(), jsonString)

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

@Serializable
data class UserModifierResponse (
    val avatar: String? = "",
    val message: String? = null,
    val userId: Long,
    val userName: String
)