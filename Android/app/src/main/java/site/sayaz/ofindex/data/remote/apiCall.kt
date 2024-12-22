package site.sayaz.ofindex.data.remote

import retrofit2.HttpException

suspend fun <T> apiCall(apiCall: suspend () -> T): Result<T> {
    return try {
        val response = apiCall()
        Result.success(response)
    } catch (e: HttpException) {
        Result.failure(Exception("HTTP Error: ${e.code()} - ${e.message()}"))
    } catch (e: Exception) {
        Result.failure(e)
    }
}