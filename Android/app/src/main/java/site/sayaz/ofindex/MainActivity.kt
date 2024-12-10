package site.sayaz.ofindex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import site.sayaz.ofindex.api.RetrofitInstance
import site.sayaz.ofindex.data.remote.ApiService
import site.sayaz.ofindex.data.repository.BookRepository
import site.sayaz.ofindex.ui.navigation.AppNavigation
import site.sayaz.ofindex.ui.theme.OFIndexTheme
import site.sayaz.ofindex.viewmodel.ReaderViewModel
import site.sayaz.ofindex.viewmodel.ReaderViewModelFactory


class MainActivity : ComponentActivity() {






    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val bookRepository = BookRepository(RetrofitInstance.apiService)


        val readerViewModel: ReaderViewModel by viewModels {
            ReaderViewModelFactory(bookRepository)
        }


        setContent { AppNavigation(readerViewModel) }
    }
}