package site.sayaz.ofindex.data.repository


import retrofit2.HttpException
import site.sayaz.ofindex.data.model.Book
import site.sayaz.ofindex.data.remote.ApiService

class ExploreRepository(private val apiService: ApiService) : BaseRepository {
    suspend fun getBooks():List<Book> {
        return try {
            val response = apiService.getBooks()
            response.body()?: emptyList()
        } catch (e: HttpException) {
            if (e.code() == 601) {
                println("get601")
                emptyList()
            } else {
                throw e
            }
        } catch (e: Exception) {
            throw e
        }
    }
}
