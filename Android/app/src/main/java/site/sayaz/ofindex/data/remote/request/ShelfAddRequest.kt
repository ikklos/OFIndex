package site.sayaz.ofindex.data.remote.request

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

@Serializable
data class ShelfAddRequest (
    val bookId: Long,
    val booklistId: Long
)