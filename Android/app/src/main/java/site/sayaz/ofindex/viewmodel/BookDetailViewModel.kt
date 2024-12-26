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
import site.sayaz.ofindex.data.model.SimpleBooklist
import site.sayaz.ofindex.data.remote.response.UserPack
import site.sayaz.ofindex.data.repository.BookDetailRepository

class BookDetailViewModel(
    private val bookDetailRepository: BookDetailRepository
) : ViewModel() {
    private val _bookDetail = MutableStateFlow(Book())
    val bookDetail: StateFlow<Book> = _bookDetail

    private val _packList = MutableStateFlow<List<Pack>>(emptyList())
    val packList: StateFlow<List<Pack>> = _packList

    private val _isLiked = MutableStateFlow(false)
    val isLiked: StateFlow<Boolean> = _isLiked

    private val _shelfList = MutableStateFlow<List<SimpleBooklist>>(emptyList())
    val shelfList: StateFlow<List<SimpleBooklist>> = _shelfList

    //图书在哪些书单里
    private val _bookInShelfList = MutableStateFlow<List<SimpleBooklist>>(emptyList())
    val bookInShelfList: StateFlow<List<SimpleBooklist>> = _bookInShelfList

    private val _userPackList = MutableStateFlow<List<UserPack>>(emptyList())
    val userPackList: StateFlow<List<UserPack>> = _userPackList

    private val _chosenPack = MutableStateFlow<Pack?>(null)
    val chosenPack: StateFlow<Pack?> = _chosenPack

    fun getBookDetail(bookId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = bookDetailRepository.getBookDetail(bookId)
            result
                .onSuccess { response ->
                    val bookDetailResponse = response.body()
                    if (bookDetailResponse != null) {
                        _bookDetail.value = Book(
                            bookId = bookDetailResponse.bookId ?: -1,
                            name = bookDetailResponse.name ?: "",
                            author = bookDetailResponse.author ?: "",
                            description = bookDetailResponse.description ?: "",
                            cover = bookDetailResponse.cover ?: "",
                            tag = bookDetailResponse.tag?.map { it ?: "" } ?: emptyList(),
                            isbn = bookDetailResponse.isbn ?: "",
                            bookClass = bookDetailResponse.bookClass ?: 0
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

    fun addPack(packId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = bookDetailRepository.addPack(packId)
            result
                .onFailure { e ->
                    Log.e(TAG, "addPack: $e")
                }
        }
    }

    fun likePack(packId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = bookDetailRepository.likePack(packId)
            result
                .onFailure { e ->
                    Log.e(TAG, "likePack: $e")
                }
        }
    }

    fun findBook(bookId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = bookDetailRepository.findBook(bookId)
            result
                .onSuccess {
                    val isInShelfResponse = it.body()
                    if (isInShelfResponse != null) {
                        _isLiked.value = isInShelfResponse.booklists.isNotEmpty()
                        _bookInShelfList.value = isInShelfResponse.booklists
                    } else {
                        Log.e(TAG, "getIsLiked: ${it.code()}")
                    }
                }
                .onFailure {
                    Log.e(TAG, "getIsLiked: $it")
                }
        }
    }

    fun getSimpleShelf() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = bookDetailRepository.getSimpleShelf()
            result
                .onSuccess {
                    val body = it.body()
                    if (body != null) {
                        _shelfList.value = body.booklists
                        println("_shelfList${_shelfList.value}")
                    } else {
                        Log.e(TAG, "getSimpleShelf: ${it.code()}")
                    }
                }
                .onFailure {
                    Log.e(TAG, "getSimpleShelf: $it")
                }
        }
    }

    fun addBook(bookId: Long, booklistId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = bookDetailRepository.shelfAdd(bookId, booklistId)
            result.onSuccess {
                val body = it.body()
                if (body != null) {
                    _isLiked.value = true
                } else {
                    Log.e(TAG, "addBook: ${it.code()}")
                }
            }.onFailure {
                Log.e(TAG, "addBook: $it")
            }

        }
    }

    fun removeBook(bookId: Long, booklistId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = bookDetailRepository.shelfRemove(bookId, booklistId)
            result.onSuccess {
                val body = it.body()
                if (body != null) {
                    _isLiked.value = false
                } else {
                    Log.e(TAG, "removeBook: ${it.code()}")
                }

            }.onFailure {
                Log.e(TAG, "removeBook: $it")
            }
        }
    }

    fun getUserPackList(bookId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = bookDetailRepository.getUserPackList(bookId)
            result.onSuccess {
                val body = it.body()
                if (body != null) {
                    _userPackList.value = body.packs
                    Log.d(TAG, "getUserPackList: ${_userPackList.value}")
                } else {
                    Log.e(TAG, "getUserPackList: ${it.code()}")
                }
            }.onFailure {
                Log.e(TAG, "getUserPackList: $it")
            }

        }
    }

    fun choosePack(pack: Pack) {
        _chosenPack.value = pack
    }


}