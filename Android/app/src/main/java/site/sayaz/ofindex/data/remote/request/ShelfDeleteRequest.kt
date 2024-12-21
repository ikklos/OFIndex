package site.sayaz.ofindex.data.remote.request

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

@Serializable
data class ShelfDeleteRequest (
    @SerialName("bookId")
    val bookID: Long,

    @SerialName("shelfId")
    val shelfID: Long
)