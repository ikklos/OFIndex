package site.sayaz.ofindex.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import retrofit2.Response
import site.sayaz.ofindex.data.model.Post
import site.sayaz.ofindex.data.remote.ApiService
import site.sayaz.ofindex.data.remote.apiCall
import site.sayaz.ofindex.data.remote.request.AddCommentRequest
import site.sayaz.ofindex.data.remote.response.AddCommentResponse
import site.sayaz.ofindex.data.remote.response.ForumCommentResponse
import site.sayaz.ofindex.data.remote.response.ForumDetailResponse
import site.sayaz.ofindex.data.remote.response.NormalResponse

class ForumDetailRepository(private val apiService: ApiService) {
    suspend fun getForumDetail(postId: Long): Result<Response<Post>> {
        return apiCall { apiService.forumPostDetail(postId) }
    }

    suspend fun getForumComment(postId: Long): Result<Response<ForumCommentResponse>> {
        return apiCall { apiService.forumComments(postId) }
    }

    suspend fun addComment(postId: Long,parentId : Long? = null,text : String): Result<Response<AddCommentResponse>> {
        Log.d(TAG,"postid:$postId parentid:$parentId text:$text")
        return apiCall { apiService.addComment(AddCommentRequest(
            postId = postId,
            parent = parentId,
            text = text
        )) }
    }

    suspend fun likeComment(commentId: Long): Result<Response<NormalResponse>> {
        println("like $commentId")
        return apiCall { apiService.likeComment(commentId) }
    }
}