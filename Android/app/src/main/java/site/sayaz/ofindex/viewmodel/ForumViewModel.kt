package site.sayaz.ofindex.viewmodel

import androidx.lifecycle.ViewModel
import site.sayaz.ofindex.data.repository.ForumRepository

class ForumViewModel(
    private val forumRepository: ForumRepository
):ViewModel() {
}