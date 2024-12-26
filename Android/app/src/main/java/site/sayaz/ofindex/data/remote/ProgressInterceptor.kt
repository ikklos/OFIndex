package site.sayaz.ofindex.data.remote

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import okio.source
import site.sayaz.ofindex.data.remote.response.ProgressResponseBody


class ProgressInterceptor(private val progressListener: (bytesRead: Long, contentLength: Long, done: Boolean) -> Unit) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        return originalResponse.newBuilder()
            .body(ProgressResponseBody(originalResponse.body!!, progressListener))
            .build()
    }
}


