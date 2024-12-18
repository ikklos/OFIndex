package site.sayaz.ofindex.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import site.sayaz.ofindex.ui.screen.auth.LoginScreen
import site.sayaz.ofindex.ui.screen.auth.RegisterScreen
import site.sayaz.ofindex.ui.screen.shelf.ReadScreen
import site.sayaz.ofindex.ui.screen.shelf.ShelfScreen
import site.sayaz.ofindex.util.TODOScreen
import site.sayaz.ofindex.viewmodel.ExploreViewModel
import site.sayaz.ofindex.viewmodel.ForumViewModel
import site.sayaz.ofindex.viewmodel.AuthViewModel
import site.sayaz.ofindex.viewmodel.ShelfViewModel

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    exploreViewModel: ExploreViewModel,
    forumViewModel: ForumViewModel,
    shelfViewModel: ShelfViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route



    Scaffold(
        bottomBar = {
            if (currentRoute in listOf(Route.explore(),Route.shelf(),Route.forum(),Route.more())) {
                BottomNavigation { route:String ->
                    navController.navigate(route)
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Route.login(),
            modifier = Modifier.padding(innerPadding)
        ) {

            // Main Navigation display bottomBar
            composable(Route.explore()) {
                TODOScreen("ex")
            }
            composable(Route.shelf()) {
                ShelfScreen(shelfViewModel,onNavigateRead = { bookID ->
                    navController.navigate(Route.read(bookID))
                })
            }
            composable(Route.forum()) {
                TODOScreen("for")
            }
            composable(Route.more()) {
                TODOScreen("sa")
            }

            // Login/Register
            composable(Route.login()) {
                LoginScreen(
                    authViewModel,
                    onNavigateRegister = {
                        navController.navigate(Route.register()) },
                    onNavigateMain = {
                        navController.navigate(Route.explore()){
                            popUpTo(Route.login()) { inclusive = true }
                        } }
                )
            }
            composable(Route.register()) {
                RegisterScreen(
                    authViewModel,
                    onNavigateLogin = {
                        navController.navigate(Route.login()) }
                )
            }


            composable(Route.read("{bookID}")) {backStackEntry ->
                val bookID = backStackEntry.arguments?.getString("bookID")
                ReadScreen(shelfViewModel,bookID?:"")
            }
        }
    }
}

