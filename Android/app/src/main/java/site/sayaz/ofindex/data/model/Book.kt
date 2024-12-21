package site.sayaz.ofindex.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Book(
    val bookId: Int,
    val name: String? = null,
    val cover: String? = null,
    val author: String? = null,
    val description: String? = null,
    val tag: String? = null,
    val isbn: String? = null,
    val bookClass: Int? = null
)

