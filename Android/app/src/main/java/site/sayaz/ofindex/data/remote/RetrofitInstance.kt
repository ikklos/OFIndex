package site.sayaz.ofindex.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import site.sayaz.ofindex.data.remote.ApiService

object RetrofitInstance {

    private const val BASE_URL = "https://example.com/api/" // 替换为实际后端地址



    private val client = OkHttpClient.Builder()
        .build()

    // 创建 Retrofit 实例
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    // 创建 ApiService 实例
    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
