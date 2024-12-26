package site.sayaz.ofindex.data.remote

import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(
    private val tokenProvider: () -> String?,
    private val tokenClearer: () -> Unit
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val token = tokenProvider()
        val response = if (token != null) {
            val newRequest = request.newBuilder()
                .addHeader("Authorization", "$token")
                .build()
            chain.proceed(newRequest)
        } else {
            chain.proceed(request)
        }

        if (response.code == 601) { // 检查响应码是否为 601
            tokenClearer() // 清空 token
        }

        return response
    }
}