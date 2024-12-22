package site.sayaz.ofindex.data.remote.response

import kotlinx.serialization.*
import site.sayaz.ofindex.data.model.Book


@Serializable
data class SearchResponse (
    val count: Long,
    val items: List<Book>,
    val message: String,
    val result: Boolean,
    val totalResult: Long
)


