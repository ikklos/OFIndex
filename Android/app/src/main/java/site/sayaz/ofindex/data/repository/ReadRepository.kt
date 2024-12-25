package site.sayaz.ofindex.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import okio.Buffer
import okio.source
import retrofit2.Response
import site.sayaz.ofindex.data.model.Pack
import site.sayaz.ofindex.data.remote.ApiService
import site.sayaz.ofindex.data.remote.apiCall
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

class ReadRepository(private val apiService: ApiService)  {
    suspend fun loadBook(bookId: Long): Result<Response<ResponseBody>> {
        return apiCall { apiService.loadBook(bookId) }
    }

    suspend fun loadPack(packId: Long): Result<Response<Pack>> {
        return apiCall { apiService.packDetail(packId) }
    }
}

