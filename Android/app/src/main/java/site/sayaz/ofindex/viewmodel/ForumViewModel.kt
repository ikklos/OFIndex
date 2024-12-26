package site.sayaz.ofindex.viewmodel


import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import site.sayaz.ofindex.data.model.Post
import site.sayaz.ofindex.data.repository.ForumRepository

class ForumViewModel(
    private val forumRepository: ForumRepository
) : ViewModel() {
    private val _forumPosts = MutableStateFlow<List<Post>>(emptyList())
    val forumPosts = _forumPosts

    // 图片上传状态
    private val _imageUploadStatus = MutableStateFlow<List<ImageUploadStatus>>(emptyList())
    val imageUploadStatus: StateFlow<List<ImageUploadStatus>> = _imageUploadStatus

    // 用户输入的状态
    private val _postData = MutableStateFlow(Post())
    val postData: StateFlow<Post> = _postData


    // 更新标题
    fun updateTitle(title: String) {
        _postData.value = _postData.value.copy(title = title)
    }

    // 更新文本内容
    fun updateText(text: String) {
        _postData.value = _postData.value.copy(description = text)
    }

    // 添加标签
    fun addTag(tag: String) {
        val newTags = _postData.value.tags.toMutableList().apply { add(tag) }
        _postData.value = _postData.value.copy(tags = newTags)
    }

    // 移除标签
    fun removeTag(tag: String) {
        val newTags = _postData.value.tags.toMutableList().apply { remove(tag) }
        _postData.value = _postData.value.copy(tags = newTags)
    }


    private var currentPage = 0L
    var isLoading = false
    var hasMore = true
    fun getForumPosts() {
        if (isLoading || (!hasMore)) return
        isLoading = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = forumRepository.getForumPosts(page = currentPage)
                result.onSuccess { response ->
                    val postsResponse = response.body()
                    if (postsResponse != null) {
                        val newPosts = postsResponse.posts.orEmpty()

                        // 如果新获取的帖子为空，说明没有更多数据
                        if (newPosts.isEmpty()) {
                            hasMore = false
                        } else {
                            // 将新获取的帖子添加到现有列表中
                            _forumPosts.value += newPosts
                            currentPage++
                        }
                    } else {
                        Log.e(TAG, "getForumPosts: ${response.code()}")
                    }
                }

                result.onFailure { e ->
                    Log.e(TAG, "getForumPosts: $e")
                }
            } finally {
                isLoading = false
            }
        }
    }

    fun addPost(postData: Post) {
        viewModelScope.launch(Dispatchers.IO) {
                val result = forumRepository.addPost(postData)
            result.onSuccess {
                if (it.body() != null){
                    Log.d(TAG, "addPost: $it")
                }else{
                    Log.e(TAG, "addPost: ${it.code()}")
                }
            }
            result.onFailure {
                Log.e(TAG, "addPost: $it")
            }
        }

    }

    fun refreshForumPosts(){
        currentPage = 0L
        hasMore = true
        _forumPosts.value = emptyList()
        getForumPosts()
    }

    fun clearAddPostScreen(){
        _postData.value = Post()
        _imageUploadStatus.value = emptyList()
    }

    // 上传图片
    fun uploadImages(imageUris: List<Uri>) {
        _imageUploadStatus.value = imageUris.map { ImageUploadStatus.Uploading(it) }
        viewModelScope.launch {
            try {
                val uploadResults = forumRepository.uploadImages(imageUris)
                _imageUploadStatus.value = uploadResults
                _postData.value = _postData.value.copy(images = uploadResults.map{
                    (it as ImageUploadStatus.Success).url?:""
                })
            } catch (e: Exception) {
                Log.e(TAG,"Error uploading images: ${e.message}")
            }
        }
    }
}

sealed class ImageUploadStatus {
    data class Uploading(val uri: Uri) : ImageUploadStatus()
    data class Success(val uri: Uri, val url: String?) : ImageUploadStatus()
    data class Error(val uri: Uri, val message: String) : ImageUploadStatus()
}

fun ImageUploadStatus.getUri(): Uri {
    return when (this) {
        is ImageUploadStatus.Uploading -> uri
        is ImageUploadStatus.Success -> uri
        is ImageUploadStatus.Error -> uri
    }
}
