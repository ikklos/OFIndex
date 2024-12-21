// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json               = Json { allowStructuredMapKeys = true }
// val searchPackResponse = json.parse(SearchPackResponse.serializer(), jsonString)

package site.sayaz.ofindex.data.remote.response

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import site.sayaz.ofindex.data.model.Pack

@Serializable
data class SearchPackResponse (
    val count: Long,
    val items: List<Pack>,
    val message: String,
    val result: Boolean
)

