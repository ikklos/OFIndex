package site.sayaz.ofindex.ui.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.authComposable(
    route: String,
    isAuthed : Boolean,
    navigateToAuth : () -> Unit,
    content: @Composable() (AnimatedContentScope.(NavBackStackEntry) -> Unit)
) {
    composable(route) {
        if (isAuthed) {
            println("authComposable is auth: $route")
            content(it)
        } else {
            println("authComposable not auth: $route")
            //navigateToAuth()
        }
    }
}