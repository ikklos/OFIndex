package site.sayaz.ofindex.viewmodel

import android.content.ContentValues.TAG
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import site.sayaz.ofindex.data.repository.ShelfRepository
import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import site.sayaz.ofindex.data.model.BookList

class ShelfViewModel(
    private val shelfRepository: ShelfRepository
) : ViewModel() {
    private val _bookListList = MutableStateFlow<List<BookList>>(emptyList())
    val bookListList: StateFlow<List<BookList>> = _bookListList

    fun getShelf() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = shelfRepository.getShelf()
            result
                .onSuccess { response ->
                    val shelfResponse = response.body()
                    if (shelfResponse != null) {
                        _bookListList.value = shelfResponse.items
                    }else{
                        Log.e(TAG, "getShelf: ${response.code()}")
                    }
                }
                .onFailure { e ->
                    Log.e(TAG, "getShelf: $e")
                }
        }
    }
}