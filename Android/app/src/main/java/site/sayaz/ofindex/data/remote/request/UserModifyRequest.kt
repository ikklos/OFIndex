package site.sayaz.ofindex.data.remote.request

// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json              = Json { allowStructuredMapKeys = true }
// val userModifyRequest = json.parse(UserModifyRequest.serializer(), jsonString)

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

@Serializable
data class UserModifyRequest (
    val avatar: String?,
    val name: String?,
    val password: String?,
    val phoneNum: String?
)