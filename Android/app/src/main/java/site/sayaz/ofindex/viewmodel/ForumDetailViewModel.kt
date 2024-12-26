package site.sayaz.ofindex.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import site.sayaz.ofindex.data.model.Comment
import site.sayaz.ofindex.data.model.Post
import site.sayaz.ofindex.data.repository.ForumDetailRepository

class ForumDetailViewModel(
    private val forumDetailRepository: ForumDetailRepository
):ViewModel() {
    private val _postDetail = MutableStateFlow(Post())
    val forumDetail = _postDetail

    private val _forumComments = MutableStateFlow(listOf<Comment>())
    val forumComments = _forumComments

    fun getPostDetail(postId:Long){
        viewModelScope.launch(Dispatchers.IO){
            val result = forumDetailRepository.getForumDetail(postId)
            result
                .onSuccess { response ->
                    val body = response.body()!!
                    _postDetail.value = body
                }
                .onFailure {e->
                    Log.e(TAG,"getPostDetail:",e)
                }
        }
    }

    fun getPostComments(postId:Long){
        viewModelScope.launch(Dispatchers.IO){
            val result = forumDetailRepository.getForumComment(postId)
            result
                .onSuccess { response ->
                    val body = response.body()!!
                    _forumComments.value = body.comments
                }
                .onFailure { e->
                    Log.e(TAG,"getPostComments:",e)
                }
        }
    }

    fun addComment(postID: Long, commentText: String,parentId : Long?) {
        viewModelScope.launch(Dispatchers.IO){
            val result = forumDetailRepository.addComment(
                postId = postID,
                parentId = parentId,
                text = commentText
            )
            result
                .onSuccess { response ->
                    getPostComments(postID)
                }
                .onFailure { e->
                    Log.e(TAG,"getPostComments:",e)
                }
        }
    }

    fun likeComment(commentId: Long){
        viewModelScope.launch(Dispatchers.IO){
            val result = forumDetailRepository.likeComment(commentId)
            result
                .onSuccess {
                    if (it.body() == null){
                        Log.e(TAG,"likeComment:${it.code()}")
                    }
                }
                .onFailure {
                    Log.e(TAG,"likeComment:",it)
                }
        }
    }




}