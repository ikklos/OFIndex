package site.sayaz.ofindex.data.remote


import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import site.sayaz.ofindex.data.model.Book
import site.sayaz.ofindex.data.remote.request.LoginRequest
import site.sayaz.ofindex.data.remote.request.RegisterRequest
import site.sayaz.ofindex.data.remote.response.SearchBookResponse
import site.sayaz.ofindex.data.remote.response.LoginResponse
import site.sayaz.ofindex.data.remote.response.RegisterResponse

interface ApiService {
    @GET("/books")
    suspend fun getBooks(): Response<List<Book>>

    @GET("/book/{id}")
    suspend fun getBook(@Path("id") id: Int): Response<SearchBookResponse>

    @POST("/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<RegisterResponse>



}