package site.sayaz.ofindex.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import site.sayaz.ofindex.data.model.Book
import site.sayaz.ofindex.data.model.Pack
import site.sayaz.ofindex.data.repository.BookDetailRepository

class BookDetailViewModel(
    private val bookDetailRepository: BookDetailRepository
) : ViewModel() {
    private val _bookDetail = MutableStateFlow(Book())
    val bookDetail: StateFlow<Book> = _bookDetail

    private val _packList = MutableStateFlow<List<Pack>>(emptyList())
    val packList: StateFlow<List<Pack>> = _packList

    fun getBookDetail(bookId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = bookDetailRepository.getBookDetail(bookId)
            result
                .onSuccess { response ->
                    val bookDetailResponse = response.body()
                    if (bookDetailResponse != null) {
                        _bookDetail.value = Book(
                            bookId = bookDetailResponse.bookId,
                            name = bookDetailResponse.name,
                            author = bookDetailResponse.author,
                            description = bookDetailResponse.description,
                            cover = bookDetailResponse.cover,
                            tag = bookDetailResponse.tag,
                            isbn = bookDetailResponse.isbn,
                            bookClass = bookDetailResponse.bookClass
                        )
                    } else {
                        Log.e(TAG, "getBookDetail: ${response.code()}")
                    }
                }
                .onFailure { e ->
                    Log.e(TAG, "getBookDetail: $e")
                }
        }
    }

    fun getPackList(bookId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = bookDetailRepository.getPackList(bookId)
            result
                .onSuccess { response ->
                    val searchPackResponse = response.body()
                    if (searchPackResponse != null) {
                        _packList.value = searchPackResponse.items
                    } else {
                        Log.e(TAG, "getPackList: ${response.code()}")
                    }
                }
                .onFailure { e ->
                    Log.e(TAG, "getPackList: $e")
                }
        }
    }

    fun addPack(packId:Long){
        viewModelScope.launch(Dispatchers.IO){
            val result = bookDetailRepository.addPack(packId)
            result
                .onFailure { e ->
                    Log.e(TAG, "addPack: $e")
                }
        }
    }

    fun likePack(packId:Long){
        viewModelScope.launch(Dispatchers.IO){
            val result = bookDetailRepository.likePack(packId)
            result
                .onFailure { e ->
                    Log.e(TAG, "likePack: $e")
                }
        }
    }


}