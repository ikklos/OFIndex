package site.sayaz.ofindex.data.model

data class Book(
    val bookId: Int,
    val name: String,
    val author: String,
    val description: String,
    val cover: String,
    val tag: String,
    val isbn: String,
    val bookClass:Int
)
