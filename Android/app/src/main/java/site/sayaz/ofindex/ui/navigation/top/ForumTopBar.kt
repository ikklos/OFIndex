package site.sayaz.ofindex.ui.navigation.top

import androidx.compose.runtime.Composable
import site.sayaz.ofindex.viewmodel.ForumViewModel

@Composable
fun ForumTopBar(
    forumViewModel: ForumViewModel
){
    BaseTopBar(
        title = "Forum",
        actions = {}
    )
}