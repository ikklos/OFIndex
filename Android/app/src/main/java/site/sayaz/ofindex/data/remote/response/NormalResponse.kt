// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json           = Json { allowStructuredMapKeys = true }
// val normalResponse = json.parse(NormalResponse.serializer(), jsonString)

package site.sayaz.ofindex.data.remote.response

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

@Serializable
data class NormalResponse (
    val message: String,
    val result: Boolean
)