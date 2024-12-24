package site.sayaz.ofindex.data.remote.response

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.BufferedSource
import okio.Okio
import okio.buffer


class ProgressResponseBody(
    private val responseBody: ResponseBody,
    private val progressListener: (bytesRead: Long, contentLength: Long, done: Boolean) -> Unit
) : ResponseBody() {

    private var bufferedSource: BufferedSource? = null

    override fun contentType(): MediaType? = responseBody.contentType()

    override fun contentLength(): Long = responseBody.contentLength()

    override fun source(): BufferedSource {
        if (bufferedSource == null) {
            bufferedSource = ProgressSource(responseBody.source()).buffer()
        }
        return bufferedSource!!
    }

    private inner class ProgressSource(
        source: okio.Source
    ) : okio.ForwardingSource(source) {

        private var totalBytesRead = 0L

        override fun read(sink: okio.Buffer, byteCount: Long): Long {
            val bytesRead = super.read(sink, byteCount)
            totalBytesRead += if (bytesRead != -1L) bytesRead else 0
            progressListener(totalBytesRead, responseBody.contentLength(), bytesRead == -1L)
            return bytesRead
        }
    }
}
