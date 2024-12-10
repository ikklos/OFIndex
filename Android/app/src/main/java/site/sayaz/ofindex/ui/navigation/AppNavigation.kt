package site.sayaz.ofindex.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import site.sayaz.ofindex.ui.screen.reader.ReaderScreen
import site.sayaz.ofindex.viewmodel.ReaderViewModel

@Composable
fun AppNavigation(
    readerViewModel: ReaderViewModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "reader") {
        composable("reader") { ReaderScreen(readerViewModel) }
    }
}
