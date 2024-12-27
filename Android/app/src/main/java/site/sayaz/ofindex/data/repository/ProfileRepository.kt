package site.sayaz.ofindex.data.repository

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import retrofit2.Response
import site.sayaz.ofindex.data.remote.ApiService
import site.sayaz.ofindex.data.remote.ImageApiService
import site.sayaz.ofindex.data.remote.apiCall
import site.sayaz.ofindex.data.remote.request.UserModifyRequest
import site.sayaz.ofindex.data.remote.response.UserInfoResponse
import site.sayaz.ofindex.data.remote.response.UserModifierResponse
import site.sayaz.ofindex.viewmodel.ImageUploadStatus

class ProfileRepository(private val apiService: ApiService, private val imageApiService: ImageApiService) {
    suspend fun getUser(): Result<Response<UserInfoResponse>> {
        return apiCall { apiService.user() }
    }

    suspend fun changeUserName(userName: String): Result<Response<UserModifierResponse>> {
        return apiCall {
            apiService.modifyUser(
                UserModifyRequest(
                    avatar = null,
                    name = userName,
                    password = null,
                    phoneNum = null
                )
            )
        }
    }
    suspend fun changePassword(password: String): Result<Response<UserModifierResponse>> {
        return apiCall {
            apiService.modifyUser(
                UserModifyRequest(
                    avatar = null,
                    name = null,
                    password = password,
                    phoneNum = null
                )
            )
        }
    }
    suspend fun changeAvatar(avatar: String): Result<Response<UserModifierResponse>> {
        return apiCall {
            apiService.modifyUser(
                UserModifyRequest(
                    avatar = avatar,
                    name = null,
                    password = null,
                    phoneNum = null
                )
            )
        }
    }

    suspend fun uploadImage(imageUri : Uri): ImageUploadStatus {
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