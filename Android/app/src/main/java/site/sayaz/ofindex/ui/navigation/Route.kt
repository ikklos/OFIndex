package site.sayaz.ofindex.ui.navigation

object Route {
    fun explore() = "explore"
    fun shelf() = "shelf"
    fun forum() = "forum"
    fun more() = "more"

    fun login() = "login"
    fun register() = "register"

    fun read() = "reader/{bookID}"
    fun read(bookID: Long) = "reader/$bookID"
    fun bookDetail() = "bookDetail/{bookID}"
    fun bookDetail(bookID: Long) = "bookDetail/$bookID"




}

