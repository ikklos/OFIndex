package site.sayaz.ofindex.data.remote.request

import kotlinx.serialization.*

@Serializable
data class ShelfRemoveRequest (
    val bookId: Long,
    val booklistId: Long
)