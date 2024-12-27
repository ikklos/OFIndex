package site.sayaz.ofindex.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import retrofit2.Response
import site.sayaz.ofindex.data.remote.ApiService
import site.sayaz.ofindex.data.remote.apiCall
import site.sayaz.ofindex.data.remote.request.ShelfAddRequest
import site.sayaz.ofindex.data.remote.request.ShelfRemoveRequest
import site.sayaz.ofindex.data.remote.response.BookDetailResponse
import site.sayaz.ofindex.data.remote.response.FindBookResponse
import site.sayaz.ofindex.data.remote.response.NormalResponse
import site.sayaz.ofindex.data.remote.response.SearchPackResponse
import site.sayaz.ofindex.data.remote.response.SimpleShelfResponse
import site.sayaz.ofindex.data.remote.response.UserPackResponse

class BookDetailRepository(private val apiService: ApiService) {
    suspend fun getBookDetail(bookId: Long): Result<Response<BookDetailResponse>> {
        return apiCall { apiService.getBook(bookId) }
    }

    suspend fun getPackList(bookId: Long): Result<Response<SearchPackResponse>> {
        return apiCall { apiService.searchPack(bookId) }
    }

    suspend fun addPack(packId: Long): Result<Response<NormalResponse>> {
        return apiCall { apiService.addPack(packId) }
    }

    suspend fun likePack(packId: Long): Result<Response<NormalResponse>> {
        return apiCall { apiService.likePack(packId) }
    }

    suspend fun shelfAdd(bookId: Long, booklistId: Long): Result<Response<NormalResponse>> {
        println("bookid:$bookId booklistid:$booklistId")
        return apiCall {
            apiService.shelfAdd(
                ShelfAddRequest(
                    bookId = bookId,
                    booklistId = booklistId
                )
            )
        }
    }

    suspend fun shelfRemove(bookId: Long, booklistId: Long): Result<Response<NormalResponse>> {
        return apiCall { apiService.shelfRemove(booklistId, bookId) }
    }

    suspend fun getSimpleShelf(): Result<Response<SimpleShelfResponse>> {
        return apiCall { apiService.simpleShelf() }
    }

    suspend fun findBook(bookId: Long): Result<Response<FindBookResponse>> {
        return apiCall { apiService.findBook(bookId) }
    }

    suspend fun getUserPackList(bookId: Long): Result<Response<UserPackResponse>> {
        var userId: Long? = null
        apiCall { apiService.user() }.onSuccess {
            userId = it.body()?.userId
            println("userid = $userId")
        }
        if (userId == null){
            return Result.failure(Exception("UserID not found"))
        }
        Log.d(TAG,"getUserPackListRep userid:$userId,bookID:$bookId")
        return apiCall { apiService.userPackList(userId!!,bookId)}
    }




}