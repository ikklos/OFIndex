package site.sayaz.ofindex.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import site.sayaz.ofindex.data.repository.DownloadResult
import site.sayaz.ofindex.data.repository.ReadRepository

class ReadViewModel(
    private val readRepository: ReadRepository
):ViewModel() {
    private val _pdfBytes = MutableStateFlow<ByteArray?>(null)
    val pdfBytes: StateFlow<ByteArray?> = _pdfBytes.asStateFlow()

    private val _downloadProgress = MutableStateFlow<Int?>(null)
    val downloadProgress: StateFlow<Int?> = _downloadProgress.asStateFlow()

    fun loadBook(bookId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            // 假设 loadBookWithProgress 是一个返回 Flow<DownloadResult> 的函数
            readRepository.loadBook(bookId).collect { result ->
                when (result) {
                    is DownloadResult.Progress -> {
                        _downloadProgress.value = result.progress
                    }
                    is DownloadResult.Success -> {
                        _pdfBytes.value = result.data
                        _downloadProgress.value = null // 下载完成，重置进度
                    }
                    is DownloadResult.Failure -> {
                        Log.e(TAG, "loadBook: ", result.error)
                        _downloadProgress.value = null // 下载失败，重置进度
                    }
                }
            }
        }
    }
}