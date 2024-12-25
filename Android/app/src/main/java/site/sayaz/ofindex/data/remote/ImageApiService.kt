package site.sayaz.ofindex.data.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import site.sayaz.ofindex.data.remote.response.ImageUploadResponse

interface ImageApiService {

    @Multipart
    @POST("upload")
    suspend fun uploadImage(
        @Part smfile: MultipartBody.Part
    ): Response<ImageUploadResponse>
}