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
import site.sayaz.ofindex.data.model.Book
import site.sayaz.ofindex.data.model.Class
import site.sayaz.ofindex.data.remote.request.SearchRequest
import site.sayaz.ofindex.data.repository.ExploreRepository

class ExploreViewModel(
    private val exploreRepository: ExploreRepository
) : ViewModel() {
    private val _searchResults = MutableStateFlow<List<Book>>(emptyList())
    val searchResults: StateFlow<List<Book>> = _searchResults

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchActive = MutableStateFlow(false)
    val searchActive: StateFlow<Boolean> = _searchActive.asStateFlow()

    private val _currentClass = MutableStateFlow(0L)
    val currentClass: StateFlow<Long> = _currentClass.asStateFlow()

    private val _showBottomSheet = MutableStateFlow(false)
    val showBottomSheet:StateFlow<Boolean> = _showBottomSheet.asStateFlow()

    private val _classList = MutableStateFlow<List<Class>>(emptyList())
    val classList: StateFlow<List<Class>> = _classList.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleSearchMode(active: Boolean) {
        _searchActive.value = active
    }

    fun updateBookClass(bookClass: Long) {
        _currentClass.value = bookClass
        toggleSearchMode(false)
        search(searchQuery.value, bookClass, true)
    }

    fun toggleBottomSheet(show:Boolean){
        _showBottomSheet.value = show
    }

    private var currentPage = 0L
    private var isLoading = false
    private var hasMore = true
    fun search(text: String, bookClass: Long, isNewSearch: Boolean = false) {
        if (isLoading || (!hasMore && !isNewSearch)) return
        if (isNewSearch) {
            _searchResults.value = emptyList()
            currentPage = 0
            hasMore = true
        }
        isLoading = true
        viewModelScope.launch(Dispatchers.IO) {
            val searchRequest = SearchRequest(
                bookClass = bookClass,
                count = 30,
                page = currentPage,
                text = text
            )
            val result = exploreRepository.search(searchRequest)
            result
                .onSuccess { response ->
                    val searchResponse = response.body()
                    if (searchResponse != null) {
                        val newResults = searchResponse.items
                        if (newResults.isEmpty()) {
                            hasMore = false
                        } else {
                            _searchResults.value += newResults
                            currentPage++
                        }
                    }else{
                        Log.e(TAG, "search: ${response.code()}")
                    }
                }
                .onFailure { e->
                    Log.e(TAG, "search: $e")
                }
            isLoading = false

        }
    }

    fun getClassList(){
        viewModelScope.launch(Dispatchers.IO){
            val result = exploreRepository.getClassList()
            result
                .onSuccess { response ->
                    val classListResponse = response.body()
                    if (classListResponse != null) {
                        _classList.value = classListResponse.items
                    }else{
                        Log.e(TAG, "getClassList: ${response.code()}")
                    }
                }
                .onFailure { e->
                    Log.e(TAG, "getClassList: $e")
                }
        }
    }
}





