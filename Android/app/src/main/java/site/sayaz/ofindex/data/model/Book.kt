package site.sayaz.ofindex.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Book(
    val bookId: Long = 0,
    val name: String? = null,
    val cover: String? = null,
    val author: String? = null,
    val description: String? = null,
    val tag: List<String> = emptyList(),
    val isbn: String? = null,
    val bookClass: Long? = null
)


