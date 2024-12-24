package site.sayaz.ofindex.data.repository

import retrofit2.Response
import site.sayaz.ofindex.data.remote.ApiService
import site.sayaz.ofindex.data.remote.apiCall
import site.sayaz.ofindex.data.remote.response.ForumPostsResponse


class ForumRepository(private val apiService: ApiService) : BaseRepository {
    suspend fun getForumPosts(): Result<Response<ForumPostsResponse>> {
        return apiCall { apiService.forumPosts() }
    }


}
