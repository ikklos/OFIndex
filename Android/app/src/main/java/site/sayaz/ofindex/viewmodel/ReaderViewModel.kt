package site.sayaz.ofindex.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import site.sayaz.ofindex.data.model.Book
import site.sayaz.ofindex.data.repository.BookRepository


class ReaderViewModel (
    private val repository: BookRepository
) : ViewModel()  {

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books

    private val _pdfContent = MutableStateFlow<ByteArray?>(null)
    val pdfContent: StateFlow<ByteArray?> = _pdfContent

    fun loadBooks() {
        viewModelScope.launch {
            _books.value = repository.fetchBooks()
        }
    }

    fun loadPdfContent(bookId: String) {
        viewModelScope.launch {
            val responseBody = repository.fetchPdfContent(bookId)
            _pdfContent.value = responseBody.bytes()
        }
    }
}