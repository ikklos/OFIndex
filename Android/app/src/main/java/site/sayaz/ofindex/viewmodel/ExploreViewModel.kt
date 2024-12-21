package site.sayaz.ofindex.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import site.sayaz.ofindex.data.repository.ExploreRepository

class ExploreViewModel(
    private val exploreRepository: ExploreRepository
):ViewModel() {

    fun getBooks() {
        viewModelScope.launch(Dispatchers.IO){
            exploreRepository.getBooks()
        }
    }

}



