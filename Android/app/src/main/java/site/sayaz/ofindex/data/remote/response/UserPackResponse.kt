// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json             = Json { allowStructuredMapKeys = true }
// val userPackResponse = json.parse(UserPackResponse.serializer(), jsonString)

package site.sayaz.ofindex.data.remote.response

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

@Serializable
data class UserPackResponse (
    val count: Long,
    val message: JsonElement? = null,
    val packs: List<UserPack>
)

@Serializable
data class UserPack (
    @SerialName("packId")
    val packID: Long? = null,

    val packLikes: Long? = null,
    val packName: String? = null,
    val shared: Boolean? = null
)