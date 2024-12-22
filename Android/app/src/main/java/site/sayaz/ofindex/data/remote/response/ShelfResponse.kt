// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json          = Json { allowStructuredMapKeys = true }
// val shelfResponse = json.parse(ShelfResponse.serializer(), jsonString)

package site.sayaz.ofindex.data.remote.response

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import site.sayaz.ofindex.data.model.Book
import site.sayaz.ofindex.data.model.BookList

@Serializable
data class ShelfResponse (
    val count: Long,
    val items: List<BookList>,
    val message: String? = null,
    val result: Boolean
)









