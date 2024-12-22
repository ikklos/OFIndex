package site.sayaz.ofindex.data.repository


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import site.sayaz.ofindex.data.model.Book
import site.sayaz.ofindex.data.remote.ApiService
import site.sayaz.ofindex.data.remote.apiCall
import site.sayaz.ofindex.data.remote.request.SearchRequest
import site.sayaz.ofindex.data.remote.response.ClassListResponse
import site.sayaz.ofindex.data.remote.response.SearchResponse

class ExploreRepository(private val apiService: ApiService) {

    suspend fun search(searchRequest: SearchRequest): Result<Response<SearchResponse>> {
        return apiCall { apiService.search(searchRequest) }
    }

    suspend fun getClassList(): Result<Response<ClassListResponse>> {
        return apiCall { apiService.classList() }
    }

}