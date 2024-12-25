package site.sayaz.ofindex.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ImageRetrofitInstance {
    private const val BASE_URL = "https://smms.app/api/v2/"
    // private const val BASE_URL = "http://10.194.189.228:8081"

    private const val TOKEN = "uEP3Bg1RJcyw2sxFKAMTo8nLHdZQUkth"

    private val client = OkHttpClient.Builder()
        .addInterceptor(TokenInterceptor(
            tokenProvider = { TOKEN },
            tokenClearer = {}
        ))
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val imageApiService: ImageApiService = retrofit.create(ImageApiService::class.java)

}