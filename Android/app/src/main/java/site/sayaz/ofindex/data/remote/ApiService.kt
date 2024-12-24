package site.sayaz.ofindex.data.remote


import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import site.sayaz.ofindex.data.remote.request.LoginRequest
import site.sayaz.ofindex.data.remote.request.RegisterRequest
import site.sayaz.ofindex.data.remote.request.SearchRequest
import site.sayaz.ofindex.data.remote.request.ShelfAddRequest
import site.sayaz.ofindex.data.remote.request.ShelfRemoveRequest
import site.sayaz.ofindex.data.remote.response.BookDetailResponse
import site.sayaz.ofindex.data.remote.response.ClassListResponse
import site.sayaz.ofindex.data.remote.response.ForumPostsResponse
import site.sayaz.ofindex.data.remote.response.LoginResponse
import site.sayaz.ofindex.data.remote.response.NormalResponse
import site.sayaz.ofindex.data.remote.response.RegisterResponse
import site.sayaz.ofindex.data.remote.response.SearchPackResponse
import site.sayaz.ofindex.data.remote.response.SearchResponse
import site.sayaz.ofindex.data.remote.response.ShelfHistoryResponse
import site.sayaz.ofindex.data.remote.response.ShelfResponse

interface ApiService {
    @POST("/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @GET("/book/{id}")
    suspend fun getBook(@Path("id") id: Long): Response<BookDetailResponse>

    @GET("/search/pack/{bookId}")
    suspend fun searchPack(@Path("bookId") bookId: Long): Response<SearchPackResponse>

    @GET("/shelf")
    suspend fun shelf(): Response<ShelfResponse>

    @POST("/shelf/history")
    suspend fun shelfHistory(): Response<ShelfHistoryResponse>

    @POST("/shelf/history/clear")
    suspend fun clearShelfHistory(): Response<NormalResponse>

    @POST("/shelf/add")
    suspend fun shelfAdd(@Body shelfAddRequest: ShelfAddRequest): Response<NormalResponse>

    @POST("/shelf/remove")
    suspend fun shelfRemove(@Body shelfAddRequest: ShelfRemoveRequest): Response<NormalResponse>

    @POST("/search")
    suspend fun search(@Body searchRequest: SearchRequest):Response<SearchResponse>

    @GET("/class")
    suspend fun classList():Response<ClassListResponse>

    @GET("/pack/copy/{packId}")
    suspend fun addPack(@Path("packId") packId:Long):Response<NormalResponse>

    @GET("/pack/like/{packId}")
    suspend fun likePack(@Path("packId") packId: Long):Response<NormalResponse>

    @GET("/load/ebook/{bookid}")
    suspend fun loadBook(@Path("bookid") bookid:Long):Response<ResponseBody>

    @GET("/forum/posts")
    suspend fun forumPosts():Response<ForumPostsResponse>


}