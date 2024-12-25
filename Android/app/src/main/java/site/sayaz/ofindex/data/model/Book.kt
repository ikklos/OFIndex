package site.sayaz.ofindex.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Book(
    val bookId: Long = 0,
    val name: String = " ",
    val cover: String = " ",
    val author: String = " ",
    val description: String = " ",
    val tag: List<String> = emptyList(),
    val isbn: String = " ",
    val bookClass: Long = 0,
    val addTime: String? = " "
)


