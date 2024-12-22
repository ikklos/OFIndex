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
    val author: String,
    val bookClass: Long,
    val bookId: Long,
    val cover: String,
    val description: String,
    val isbn: String,
    val message: String,
    val name: String,
    val result: Boolean,
    val tag: String
)