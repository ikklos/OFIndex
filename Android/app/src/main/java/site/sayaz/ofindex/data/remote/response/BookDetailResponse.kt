// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json               = Json { allowStructuredMapKeys = true }
// val bookDetailResponse = json.parse(BookDetailResponse.serializer(), jsonString)

package site.sayaz.ofindex.data.remote.response

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

@Serializable
data class BookDetailResponse (
    val author: String? = null,
    val bookClass: Long? = null,
    val bookId: Long? = null,
    val cover: String? = null,
    val description: String? = null,
    val isbn: String? = null,
    val message: String? = null,
    val name: String? = null,
    val tag: List<String?>? = null
)