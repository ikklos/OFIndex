package site.sayaz.ofindex.data.repository

import android.content.Context
import retrofit2.Response
import site.sayaz.ofindex.data.remote.ApiService
import site.sayaz.ofindex.data.remote.apiCall
import site.sayaz.ofindex.data.remote.response.ShelfResponse
import java.io.InputStream


class ShelfRepository(private val apiService: ApiService) : BaseRepository {
    suspend fun getShelf(): Result<Response<ShelfResponse>> {
        return apiCall {  apiService.shelf() }
    }
}
