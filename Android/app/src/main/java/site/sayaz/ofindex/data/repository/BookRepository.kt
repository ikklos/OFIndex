package site.sayaz.ofindex.data.repository

import okhttp3.ResponseBody
import site.sayaz.ofindex.data.remote.ApiService

class BookRepository(private val api: ApiService) {
    suspend fun fetchBooks() = api.getBooks()
    suspend fun fetchPdfContent(bookId: String): ResponseBody = api.getPdfContent(bookId)
}