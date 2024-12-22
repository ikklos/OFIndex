package site.sayaz.ofindex.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import site.sayaz.ofindex.ui.navigation.bottom.BottomNavigation
import site.sayaz.ofindex.ui.navigation.top.ExploreTopBar
import site.sayaz.ofindex.ui.navigation.top.ForumTopBar
import site.sayaz.ofindex.ui.navigation.top.MoreTopBar
import site.sayaz.ofindex.ui.navigation.top.ShelfTopBar
import site.sayaz.ofindex.ui.screen.auth.LoginScreen
import site.sayaz.ofindex.ui.screen.auth.RegisterScreen
import site.sayaz.ofindex.ui.screen.explore.ExploreScreen
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

    val isAuthed by authViewModel.isAuthed.collectAsState(initial = false)
    val navToAuth = {
        navController.navigate(Route.login())
    }

    LaunchedEffect(key1 = isAuthed) {
        if (!isAuthed ) {
            navToAuth()
        }

    }
    Scaffold(
        bottomBar = {
            if (currentRoute in listOf(Route.explore(),Route.shelf(),Route.forum(),Route.more())) {
                BottomNavigation { route:String ->
                    navController.navigate(route)
                }
            }
        },
        topBar = {
            when(currentRoute){
                Route.explore() -> ExploreTopBar(exploreViewModel)
                Route.shelf() -> ShelfTopBar(shelfViewModel)
                Route.forum() -> ForumTopBar(forumViewModel)
                Route.more() -> MoreTopBar()
                else -> {}
            }
        },

    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Route.explore(),
            modifier = Modifier.padding(innerPadding)
        ) {
            // Main Navigation display bottomBar
            composable(Route.explore()){
                ExploreScreen(
                    exploreViewModel,
                    onNavigateBookDetail = {
                    }
                )
            }
            composable(Route.shelf()){
                ShelfScreen(shelfViewModel,onNavigateRead = { bookID ->
                    navController.navigate(Route.read(bookID))
                })
            }
            composable(Route.forum()){
                TODOScreen("for")
            }
            composable(Route.more()){
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

            composable(Route.read("{bookID}")){
                val bookID = it.arguments?.getString("bookID")
                ReadScreen(shelfViewModel,bookID?:"")
            }
        }
    }
}



