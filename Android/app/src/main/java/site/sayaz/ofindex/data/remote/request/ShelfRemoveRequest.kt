package site.sayaz.ofindex.data.remote.request

import kotlinx.serialization.*

@Serializable
data class ShelfRemoveRequest (
    @SerialName("bookId")
    val bookID: Long,

    @SerialName("shelfId")
    val shelfID: Long
)