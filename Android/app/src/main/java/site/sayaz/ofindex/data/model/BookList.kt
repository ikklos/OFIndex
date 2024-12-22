package site.sayaz.ofindex.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import site.sayaz.ofindex.data.model.Book


@Serializable
data class BookList (
    val books: List<BookListItem>,
    val count: Long,
    val index: Long? = null,
    val name: String
)

@Serializable
data class BookListItem (
    val addTime: String? = null,
    val bookId: Long? = null,
    val name: String? = null
)