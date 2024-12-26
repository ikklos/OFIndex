package site.sayaz.ofindex.data.repository

import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.util.Log
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
import site.sayaz.ofindex.viewmodel.ImageUploadStatus
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
            pictures = post.images,
            tags = post.tags,
            text = post.description?:"error no text",
            title = post.title?:"error no title"
        )
        println(addPostRequest)
        return apiCall { apiService.addPost(addPostRequest) }
    }

    suspend fun uploadImages(imageUris: List<Uri>): List<ImageUploadStatus> {
        return withContext(Dispatchers.IO) {
            imageUris.map { imageUri ->
                run {
                    uploadImage(imageUri)
                }
            }
        }
    }

    suspend fun uploadImage(imageUri : Uri):ImageUploadStatus {
        return try{
            val result = imageApiService.uploadImage(imageUri).await()
            result.fold(
                onSuccess = {
                    Log.d(TAG,"success  ${it.url}")
                    ImageUploadStatus.Success(imageUri,it.url)
            }, onFailure = {
                    ImageUploadStatus.Error(imageUri,it.message ?: "Unknown error")
                })
        } catch (e: Exception) {
            ImageUploadStatus.Error(imageUri,e.message ?: "Unknown error")
        }
    }


}


