package site.sayaz.ofindex.data.remote.request

import kotlinx.serialization.*

@Serializable
data class SearchRequest(
    val bookClass: Long,
    val count: Long,
    val page: Long,
    val text: String
)