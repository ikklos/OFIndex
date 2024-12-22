// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json              = Json { allowStructuredMapKeys = true }
// val classListResponse = json.parse(ClassListResponse.serializer(), jsonString)

package site.sayaz.ofindex.data.remote.response

import kotlinx.serialization.*
import site.sayaz.ofindex.data.model.Class

@Serializable
data class ClassListResponse (
    val count: Long,
    val items: List<Class>,
    val message: String,
    val result: Boolean
)

