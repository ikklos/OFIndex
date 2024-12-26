package site.sayaz.ofindex.data.remote

import android.net.Uri
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import site.sayaz.ofindex.data.remote.response.ImageUploadResponse
import java.io.File

interface ImageApiService {
    suspend fun uploadImage(uri: Uri): Deferred<Result<ImageUploadResponse>>
}