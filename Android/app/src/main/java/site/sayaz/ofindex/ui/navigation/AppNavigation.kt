package site.sayaz.ofindex.ui.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import site.sayaz.ofindex.ui.screen.bookDetail.BookDetailScreen
import site.sayaz.ofindex.ui.screen.explore.ExploreScreen
import site.sayaz.ofindex.ui.screen.forum.AddPostScreen
import site.sayaz.ofindex.ui.screen.forum.ForumDetailScreen
import site.sayaz.ofindex.ui.screen.forum.ForumScreen
import site.sayaz.ofindex.ui.screen.shelf.ReadScreen
import site.sayaz.ofindex.ui.screen.shelf.ShelfScreen
import site.sayaz.ofindex.util.TODOScreen
import site.sayaz.ofindex.viewmodel.ExploreViewModel
import site.sayaz.ofindex.viewmodel.ForumViewModel
import site.sayaz.ofindex.viewmodel.AuthViewModel
import site.sayaz.ofindex.viewmodel.BookDetailViewModel
import site.sayaz.ofindex.viewmodel.ForumDetailViewModel
import site.sayaz.ofindex.viewmodel.ReadViewModel
import site.sayaz.ofindex.viewmodel.ShelfViewModel

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    exploreViewModel: ExploreViewModel,
    forumViewModel: ForumViewModel,
    shelfViewModel: ShelfViewModel,
    bookDetailViewModel: BookDetailViewModel,
    readViewModel: ReadViewModel,
    forumDetailViewModel: ForumDetailViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isAuthed by authViewModel.isAuthed.collectAsState(initial = false)
    val navToAuth = {
        navController.navigate(Route.login())
    }
    LaunchedEffect(key1 = isAuthed) {
        if (!isAuthed) {
            navToAuth()
        }

    }


    Scaffold(
        bottomBar = {
            if (currentRoute in listOf(
                    Route.explore(),
                    Route.shelf(),
                    Route.forum(),
                    Route.more()
                )
            ) {
                BottomNavigation { route: String ->
                    navController.navigate(route)
                }
            }
        },
        topBar = {
            when (currentRoute) {
                Route.explore() -> ExploreTopBar(exploreViewModel)
                Route.shelf() -> ShelfTopBar(shelfViewModel)
                Route.forum() -> ForumTopBar(forumViewModel,
                    onNavigateAddPost = {
                        navController.navigate(Route.addPost())
                    })
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
            composable(Route.explore()) {
                ExploreScreen(
                    exploreViewModel,
                    onNavigateBookDetail = { bookID ->
                        navController.navigate(Route.bookDetail(bookID.toString()))
                    }
                )
            }
            composable(Route.shelf()) {
                ShelfScreen(shelfViewModel,
                    onNavigateRead = { bookID ->
                        navController.navigate(Route.read(bookID.toString()))
                    },
                    onNavigateBookDetail = { bookID ->
                        navController.navigate(Route.bookDetail(bookID.toString()))
                    })
            }
            composable(Route.forum()) {
                ForumScreen(
                    forumViewModel,
                    onNavigateForumDetail = {
                        navController.navigate(Route.forumDetail(it.toString()))
                    }
                )
            }
            composable(Route.more()) {
                TODOScreen("sa")
            }

            // Login/Register
            composable(Route.login()) {
                LoginScreen(
                    authViewModel,
                    onNavigateRegister = {
                        navController.navigate(Route.register())
                    },
                    onNavigateMain = {
                        navController.navigate(Route.explore()) {
                            popUpTo(Route.login()) { inclusive = true }
                        }
                    }
                )
            }
            composable(Route.register()) {
                RegisterScreen(
                    authViewModel,
                    onNavigateLogin = {
                        navController.navigate(Route.login())
                    }
                )
            }

            composable(Route.read()) {
                val bookID = it.arguments?.getString("bookID")
                if (bookID != null) {
                    ReadScreen(
                        readViewModel, bookID.toLong(),
                        onNavigateBack = {
                            navController.popBackStack()
                        }
                    )
                } else {
                    Toast.makeText(LocalContext.current, "bookID is null", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            composable(Route.bookDetail()) {
                val bookID = it.arguments?.getString("bookID")
                if (bookID != null) {
                    BookDetailScreen(bookDetailViewModel, bookID.toLong(),
                        onNavigateRead = { id ->
                            navController.navigate(Route.read(id.toString()))
                        },
                        onNavigateBack = {
                            navController.popBackStack()
                        })
                } else {
                    Toast.makeText(LocalContext.current, "bookID is null", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            composable(Route.forumDetail()) {
                val postID = it.arguments?.getString("postID")
                if (postID != null) {
                    ForumDetailScreen(forumDetailViewModel,postID.toLong())
                } else{
                    Toast.makeText(LocalContext.current, "postID is null", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            composable(Route.addPost()) {
                AddPostScreen(forumViewModel) {
                    navController.popBackStack()
                }
            }
        }
    }
}



