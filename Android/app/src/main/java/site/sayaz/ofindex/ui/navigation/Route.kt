package site.sayaz.ofindex.ui.navigation

object Route {
    fun explore() = "explore"
    fun shelf() = "shelf"
    fun forum() = "forum"
    fun more() = "more"
    fun profile() = "profile"

    fun login() = "login"
    fun register() = "register"

    fun read() = "reader/{bookID}/{packID}"
    fun read(bookID: String,packID: String) = "reader/$bookID/$packID"
    fun bookDetail() = "bookDetail/{bookID}"
    fun bookDetail(bookID: String) = "bookDetail/$bookID"
    fun forumDetail() = "forumDetail/{postID}"
    fun forumDetail(postID: String) = "forumDetail/$postID"
    fun addPost() = "addPost"




}

