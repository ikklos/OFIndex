package site.sayaz.ofindex.data.remote.response

// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json     = Json { allowStructuredMapKeys = true }
// val response = json.parse(Response.serializer(), jsonString)



import kotlinx.serialization.*
import kotlinx.serialization.json.*
import site.sayaz.ofindex.data.model.SimpleBooklist

@Serializable
data class FindBookResponse (
    val booklists: List<SimpleBooklist>,
    val count: Long,
    val message: JsonElement? = null
)

