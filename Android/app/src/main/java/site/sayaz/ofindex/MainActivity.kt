package site.sayaz.ofindex

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import site.sayaz.ofindex.data.remote.RetrofitInstance
import site.sayaz.ofindex.data.repository.ExploreRepository
import site.sayaz.ofindex.data.repository.ForumRepository
import site.sayaz.ofindex.data.repository.AuthRepository
import site.sayaz.ofindex.data.repository.BookDetailRepository
import site.sayaz.ofindex.data.repository.ReadRepository
import site.sayaz.ofindex.data.repository.ShelfRepository
import site.sayaz.ofindex.ui.navigation.AppNavigation
import site.sayaz.ofindex.viewmodel.ExploreViewModel
import site.sayaz.ofindex.viewmodel.ForumViewModel
import site.sayaz.ofindex.viewmodel.AuthViewModel
import site.sayaz.ofindex.viewmodel.BookDetailViewModel
import site.sayaz.ofindex.viewmodel.ReadViewModel
import site.sayaz.ofindex.viewmodel.ShelfViewModel
import site.sayaz.ofindex.viewmodel.ViewModelFactory


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apiService = RetrofitInstance.apiService

        val loginRep = AuthRepository(apiService,applicationContext)
        val exploreRep = ExploreRepository(apiService)
        val forumRep = ForumRepository(apiService)
        val shelfRep = ShelfRepository(apiService)
        val bookDetailRep = BookDetailRepository(apiService)
        val readRep = ReadRepository(apiService)

        val authViewModel: AuthViewModel by viewModels{
            ViewModelFactory(AuthViewModel::class.java){
                AuthViewModel(loginRep)
            }
        }
        val exploreViewModel: ExploreViewModel by viewModels{
            ViewModelFactory(ExploreViewModel::class.java){
                ExploreViewModel(exploreRep)
            }
        }
        val forumViewModel: ForumViewModel by viewModels{
            ViewModelFactory(ForumViewModel::class.java){
                ForumViewModel(forumRep)
            }
        }
        val shelfViewModel: ShelfViewModel by viewModels{
            ViewModelFactory(ShelfViewModel::class.java){
                ShelfViewModel(shelfRep)
            }
        }
        val bookDetailViewModel: BookDetailViewModel by viewModels{
            ViewModelFactory(BookDetailViewModel::class.java){
                BookDetailViewModel(bookDetailRep)
            }
        }
        val readViewModel: ReadViewModel by viewModels{
            ViewModelFactory(ReadViewModel::class.java){
                ReadViewModel(readRep)
            }
        }



        setContent { AppNavigation(
            authViewModel = authViewModel,
            exploreViewModel = exploreViewModel,
            forumViewModel = forumViewModel,
            shelfViewModel = shelfViewModel,
            bookDetailViewModel = bookDetailViewModel,
            readViewModel = readViewModel
        ) }
    }
}