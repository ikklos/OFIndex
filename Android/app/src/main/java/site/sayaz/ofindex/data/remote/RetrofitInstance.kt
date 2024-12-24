package site.sayaz.ofindex.data.remote

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient

object RetrofitInstance {

    private const val BASE_URL = "http://10.194.189.228:8080"
    //private const val BASE_URL = "http://175.178.5.83:8080"

    private val _token = MutableStateFlow<String?>("")
    val token: StateFlow<String?> get() = _token.asStateFlow()

    private val client = OkHttpClient.Builder()
        .addInterceptor(TokenInterceptor(
            { _token.value },
            { _token.value = null }
        ))
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)

    fun setToken(newToken: String?) {
        println("token:$newToken")
        _token.value = newToken
    }

    fun hasToken(): Boolean {
        return _token.value != null
    }
}