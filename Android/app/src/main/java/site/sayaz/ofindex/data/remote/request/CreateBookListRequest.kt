// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json                  = Json { allowStructuredMapKeys = true }
// val createBookListRequest = json.parse(CreateBookListRequest.serializer(), jsonString)

package site.sayaz.ofindex.data.remote.request

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

@Serializable
data class CreateBookListRequest (
    /**
     * 名称
     */
    val name: String
)