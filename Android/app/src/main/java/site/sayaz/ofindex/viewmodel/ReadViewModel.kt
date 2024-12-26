package site.sayaz.ofindex.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.pager.PagerState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import site.sayaz.ofindex.data.model.Pack
import site.sayaz.ofindex.data.repository.ReadRepository

class ReadViewModel(
    private val readRepository: ReadRepository
) : ViewModel() {
    private val _pdfBytes = MutableStateFlow<ByteArray?>(null)
    val pdfBytes: StateFlow<ByteArray?> = _pdfBytes.asStateFlow()

    private val _downloadProgress = MutableStateFlow(0f) // 0 to 1 float
    val downloadProgress = _downloadProgress.asStateFlow()

    private val _isBottomBarVisible = MutableStateFlow(false)
    val isBottomBarVisible: StateFlow<Boolean> = _isBottomBarVisible.asStateFlow()

    private val _isTopBarVisible = MutableStateFlow(false)
    val isTopBarVisible: StateFlow<Boolean> = _isTopBarVisible.asStateFlow()

    private val _selectedTab = MutableStateFlow<Int>(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _pack = MutableStateFlow<Pack?>(null)
    val pack: StateFlow<Pack?> = _pack.asStateFlow()



    fun toggleBottomBarVisibility(isBottomBarVisible: Boolean) {
        _isBottomBarVisible.value = isBottomBarVisible
    }

    fun toggleTopBarVisibility(isTopBarVisible: Boolean) {
        _isTopBarVisible.value = isTopBarVisible
    }

    fun selectTab(index: Int) {
        _selectedTab.value = index
    }

    fun loadBook(bookId: Long) {
        _downloadProgress.value = 0f
        _pdfBytes.value = null

        viewModelScope.launch(Dispatchers.IO) {
            readRepository.loadBook(bookId).onSuccess { response ->
                val body = response.body()
                if (body != null) {
                    _pdfBytes.value = body.bytes()
                }
            }.onFailure {
                // Handle failure, if needed
                _downloadProgress.value = 0f
            }
        }
    }


    fun loadPack(packID: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            readRepository.loadPack(packID).onSuccess { response ->
                val body = response.body()
                if (body != null) {
                    _pack.value = body
                    Log.d(TAG, "loadPack: $body")
                }
                else{
                    Log.e(TAG, "loadPack: ${response.code()}")
                }
            }.onFailure {
                Log.e(TAG, "loadPack: $it")
            }
        }
    }

    fun scrollToPage(page: Int, pagerState: PagerState) {
        viewModelScope.launch {
            pagerState.scrollToPage(page)
        }
    }


}