package site.sayaz.ofindex.data.remote.response

// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json                = Json { allowStructuredMapKeys = true }
// val simpleShelfResponse = json.parse(SimpleShelfResponse.serializer(), jsonString)



import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import site.sayaz.ofindex.data.model.SimpleBooklist

@Serializable
data class SimpleShelfResponse (
    val booklists: List<SimpleBooklist>,
    val count: Long,
    val message: JsonElement? = null
)


