package site.sayaz.ofindex.data.remote

import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient

object RetrofitInstance {

    // private const val BASE_URL = "http://10.194.189.228:3000"
    private const val BASE_URL = "http://175.178.5.83:8080"


    private var token: String? = null

    private val client = OkHttpClient.Builder()
        .addInterceptor(TokenInterceptor { token })
        .build()



    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)

    fun setToken(newToken: String) {
        token = newToken
    }
}
