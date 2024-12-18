package site.sayaz.ofindex.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import site.sayaz.ofindex.data.repository.ShelfRepository
import android.content.Context
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShelfViewModel(
    private val shelfRepository: ShelfRepository
):ViewModel() {
    private val _pdfBytes = MutableStateFlow<ByteArray?>(null)
    val pdfBytes: StateFlow<ByteArray?> get() = _pdfBytes

    fun loadPdf(context: Context, bookID:String){
        viewModelScope.launch(Dispatchers.IO) {
//            val bytes = shelfRepository.loadPdf(context,bookID)
//            _pdfBytes.value = bytes
        }
    }
}