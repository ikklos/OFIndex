package site.sayaz.ofindex.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import okio.Buffer
import okio.source
import retrofit2.Response
import site.sayaz.ofindex.data.remote.ApiService
import site.sayaz.ofindex.data.remote.apiCall
import java.io.ByteArrayOutputStream
import java.io.IOException

class ReadRepository(private val apiService: ApiService)  {
    suspend fun loadBook(bookId: Long): Flow<DownloadResult> = flow {
        try {
            val response = apiService.loadBook(bookId)
            if (response.isSuccessful && response.body() != null) {
                val contentLength = response.body()?.contentLength()
                val source = response.body()?.byteStream()?.source() ?: throw IOException("Null source")
                val buffer = Buffer()
                var totalRead = 0L
                while (true) {
                    val read = source.read(buffer, 8192)
                    if (read == -1L) break
                    totalRead += read
                    if (contentLength != null && contentLength > 0) {
                        val progress = (100 * totalRead / contentLength).toInt()
                        emit(DownloadResult.Progress(progress))
                    }
                }
                val byteArray = buffer.readByteArray()
                emit(DownloadResult.Success(byteArray))
            } else {
                emit(DownloadResult.Failure(Throwable("Failed to load book")))
            }
        } catch (e: Exception) {
            emit(DownloadResult.Failure(e))
        }
    }.flowOn(Dispatchers.IO)
}

sealed class DownloadResult {
    data class Progress(val progress: Int) : DownloadResult()
    data class Success(val data: ByteArray) : DownloadResult()
    data class Failure(val error: Throwable) : DownloadResult()
}