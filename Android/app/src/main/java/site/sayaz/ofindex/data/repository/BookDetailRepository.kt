package site.sayaz.ofindex.data.repository

import retrofit2.Response
import site.sayaz.ofindex.data.remote.ApiService
import site.sayaz.ofindex.data.remote.apiCall
import site.sayaz.ofindex.data.remote.request.SearchRequest
import site.sayaz.ofindex.data.remote.request.ShelfAddRequest
import site.sayaz.ofindex.data.remote.response.BookDetailResponse
import site.sayaz.ofindex.data.remote.response.NormalResponse
import site.sayaz.ofindex.data.remote.response.SearchPackResponse
import site.sayaz.ofindex.data.remote.response.SearchResponse
import site.sayaz.ofindex.data.remote.response.ShelfResponse

class BookDetailRepository(private val apiService: ApiService) {
    suspend fun getBookDetail(bookId : Long): Result<Response<BookDetailResponse>> {
        return apiCall { apiService.getBook(bookId)}
    }

    suspend fun getPackList(bookId: Long):Result<Response<SearchPackResponse>>{
        return apiCall { apiService.searchPack(bookId) }
    }

    suspend fun addPack(packId: Long):Result<Response<NormalResponse>>{
        return apiCall { apiService.addPack(packId) }
    }

    suspend fun likePack(packId: Long):Result<Response<NormalResponse>>{
        return apiCall { apiService.likePack(packId) }
    }

    suspend fun shelfAdd(bookId: Long,booklistId : Long):Result<Response<NormalResponse>>{
        return apiCall { apiService.shelfAdd(ShelfAddRequest(
            bookId = bookId,
            booklistId = booklistId
        )) }
        //TODO 拿不到booklistId
    }

    suspend fun getShelf():Result<Response<ShelfResponse>>{
        return apiCall { apiService.shelf() }
    }




}