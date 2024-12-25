package site.sayaz.ofindex.data.remote.request

import kotlinx.serialization.*

@Serializable
data class ShelfRemoveRequest (
    @SerialName("bookId")
    val bookID: Long,
    val booklistId: Long
)