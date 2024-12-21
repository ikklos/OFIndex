package site.sayaz.ofindex.data.model

data class Book(
    val bookId: Int,
    val name: String,
    val cover: String,
    val author: String = "no author",
    val description: String = "no description",
    val tag: String = "no tag",
    val isbn: String = "00000000",
    val bookClass:Int = 0
)
