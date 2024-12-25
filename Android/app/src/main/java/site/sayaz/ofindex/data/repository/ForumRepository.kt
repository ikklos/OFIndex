package site.sayaz.ofindex.data.repository

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import site.sayaz.ofindex.data.model.Post
import site.sayaz.ofindex.data.remote.ApiService
import site.sayaz.ofindex.data.remote.ImageApiService
import site.sayaz.ofindex.data.remote.apiCall
import site.sayaz.ofindex.data.remote.request.AddPostRequest
import site.sayaz.ofindex.data.remote.response.AddPostResponse
import site.sayaz.ofindex.data.remote.response.ForumPostsResponse
import site.sayaz.ofindex.data.remote.response.ImageUploadResponse
import java.io.File
import java.io.IOException


class ForumRepository(
    private val apiService: ApiService,
    private val imageApiService: ImageApiService,
    private val context: Context
) : BaseRepository {
    suspend fun getForumPosts(page: Long): Result<Response<ForumPostsResponse>> {
        return apiCall { apiService.forumPosts(page = page) }
    }

    suspend fun addPost(post: Post,
    ): Result<Response<AddPostResponse>> {
        val addPostRequest = AddPostRequest(
            picture = post.images ?: emptyList(),
            tags = post.tags?: emptyList(),
            text = post.description?:"error no text",
            title = post.title?:"error no title"
        )
        return apiCall { apiService.addPost(addPostRequest) }
    }

    suspend fun uploadImages(imageUris: List<Uri>): List<Result<Response<ImageUploadResponse>>> {
        return withContext(Dispatchers.IO) {
            imageUris.map { imageUri ->
                run {
                    uploadImage(imageUri)
                }
            }
        }
    }

    suspend fun uploadImage(imageUri: Uri): Result<Response<ImageUploadResponse>> {
        return apiCall { // 从 Uri 获取文件流
            val inputStream = context.contentResolver.openInputStream(imageUri)
                ?: throw IOException("Failed to open input stream for the image")

            // 创建临时文件以保存图片
            val file = File.createTempFile("image", ".jpg", context.cacheDir).apply {
                outputStream().use { output ->
                    inputStream.copyTo(output)
                }
            }
            // 将文件转换为 MultipartBody.Part
            val requestFile: RequestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val body: MultipartBody.Part =
                MultipartBody.Part.createFormData("smfile", file.name, requestFile)

            // 调用 API 上传图片
            imageApiService.uploadImage(body)
        }
    }


}
