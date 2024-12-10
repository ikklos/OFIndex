package site.sayaz.ofindex.data.remote

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import site.sayaz.ofindex.data.model.Book

interface ApiService {
    @GET("/books")
    suspend fun getBooks(): List<Book>

    @GET("/books/{bookId}/content")
    suspend fun getPdfContent(@Path("bookId") bookId: String): ResponseBody

}