package site.sayaz.ofindex.data.repository

import retrofit2.Response
import site.sayaz.ofindex.data.model.Post
import site.sayaz.ofindex.data.remote.ApiService
import site.sayaz.ofindex.data.remote.apiCall
import site.sayaz.ofindex.data.remote.response.ForumCommentResponse
import site.sayaz.ofindex.data.remote.response.ForumDetailResponse

class ForumDetailRepository(private val apiService: ApiService) {
    suspend fun getForumDetail(postId: Long): Result<Response<Post>> {
        return apiCall { apiService.forumPostDetail(postId) }
    }

    suspend fun getForumComment(postId: Long): Result<Response<ForumCommentResponse>> {
        return apiCall { apiService.forumComments(postId) }
    }
}