package site.sayaz.ofindex.data.remote.response

import kotlinx.serialization.*
import site.sayaz.ofindex.data.model.Book


@Serializable
data class SearchResponse (
    val count: Long?,
    val items: List<SearchBookItem>?,
    val message: String?,
    val result: Boolean?,
    val totalResult: Long?
)

@Serializable
data class SearchBookItem (
    val id: Long?,
    val name: String?,
    val author: String?,
    val description: String?,
    val cover: String?,
    val tags: List<String>?
)


