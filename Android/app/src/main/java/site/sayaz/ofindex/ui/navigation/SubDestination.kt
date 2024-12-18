package site.sayaz.ofindex.ui.navigation

import androidx.annotation.RequiresPermission
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import site.sayaz.ofindex.ui.screen.shelf.ReadScreen
import site.sayaz.ofindex.util.TODOScreen
import site.sayaz.ofindex.viewmodel.ExploreViewModel
import site.sayaz.ofindex.viewmodel.ForumViewModel
import site.sayaz.ofindex.viewmodel.ShelfViewModel



fun NavGraphBuilder.subDestination(
    navController: NavHostController,
    exploreViewModel: ExploreViewModel,
    shelfViewModel: ShelfViewModel,
    forumViewModel: ForumViewModel
){

}