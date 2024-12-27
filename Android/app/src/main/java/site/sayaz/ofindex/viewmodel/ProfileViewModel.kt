package site.sayaz.ofindex.viewmodel

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import site.sayaz.ofindex.data.model.User
import site.sayaz.ofindex.data.repository.ProfileRepository

class ProfileViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {
    private val _user = MutableStateFlow<User>(User())
    val user = _user

    private val _avatarStatus = MutableStateFlow<AvatarStatus>(AvatarStatus.Loading)
    val avatarStatus = _avatarStatus

    fun getUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = profileRepository.getUser()
            result.onSuccess {
                val body = it.body()
                if (body != null) {
                    _user.value = User(
                        avatar = body.avatar, userId = body.userId, userName = body.userName
                    )
                    _avatarStatus.value = AvatarStatus.Success
                } else {
                    Log.e(TAG, "getUser: ${it.code()}")
                }
            }.onFailure {
                Log.e(TAG, "getUser: ${it.message}")
            }
        }
    }

    fun changeUserName(userName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = profileRepository.changeUserName(userName)
            result.onSuccess {
                val body = it.body()
                if (body != null) {
                    _user.value = User(
                        avatar = body.avatar, userId = body.userId, userName = body.userName
                    )
                } else {
                    Log.e(TAG, "changeUser: ${it.code()}")
                }
            }.onFailure {
                Log.e(TAG, "changeUser: ${it.message}")
            }
        }
    }

    fun changePassword(password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = profileRepository.changePassword(password)
            result.onSuccess {
                val body = it.body()
                if (body != null) {
                    _user.value = User(
                        avatar = body.avatar, userId = body.userId, userName = body.userName
                    )
                } else {
                    Log.e(TAG, "changeUser: ${it.code()}")
                }
            }.onFailure {
                Log.e(TAG, "changeUser: ${it.message}")
            }
        }
    }


    fun changeAvatar(avatar: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            _avatarStatus.value = AvatarStatus.Loading
            val imageUploadResult = profileRepository.uploadImage(avatar)
            if (imageUploadResult is ImageUploadStatus.Success) {
                val result = profileRepository.changeAvatar(imageUploadResult.url?:"")
                result.onSuccess {
                    val body = it.body()
                    if (body != null) {
                        _user.value = User(
                            avatar = body.avatar, userId = body.userId, userName = body.userName
                        )
                        _avatarStatus.value = AvatarStatus.Success
                    } else {
                        Log.e(TAG, "changeUser: ${it.code()}")
                        _avatarStatus.value = AvatarStatus.Error("Upload avatar failed")
                    }
                }.onFailure {
                    Log.e(TAG, "changeUser: ${it.message}")
                    _avatarStatus.value = AvatarStatus.Error("Upload avatar failed")
                }
            } else {
                if (imageUploadResult is ImageUploadStatus.Error) {
                    _avatarStatus.value = AvatarStatus.Error(imageUploadResult.message)
                }
            }
        }
    }

}

sealed class AvatarStatus{
    data object Loading : AvatarStatus()
    data object Success : AvatarStatus()
    data class Error(val message: String) : AvatarStatus()
}