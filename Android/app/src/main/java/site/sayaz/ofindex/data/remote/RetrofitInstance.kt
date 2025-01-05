package site.sayaz.ofindex.data.remote

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import site.sayaz.ofindex.data.remote.Config.BASE_URL

object RetrofitInstance {



    private val _token = MutableStateFlow<String?>("")
    val token: StateFlow<String?> get() = _token.asStateFlow()

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> get() = _progress.asStateFlow()


    private val client = OkHttpClient.Builder()
        .addInterceptor(TokenInterceptor(
            { _token.value },
            { _token.value = null }
        ))
        .addInterceptor(ProgressInterceptor { bytesRead, contentLength, done ->
            val progress =
                if (contentLength > 0) bytesRead.toFloat() / contentLength.toFloat() else 0f
            _progress.value = progress
        }
        )
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