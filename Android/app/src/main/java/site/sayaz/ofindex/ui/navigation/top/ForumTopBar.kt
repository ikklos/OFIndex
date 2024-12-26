package site.sayaz.ofindex.ui.navigation.top

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import site.sayaz.ofindex.R
import site.sayaz.ofindex.ui.components.BaseTopBar
import site.sayaz.ofindex.viewmodel.ForumViewModel

@Composable
fun ForumTopBar(
    forumViewModel: ForumViewModel,
    onNavigateAddPost: () -> Unit
){
    BaseTopBar(
        title = "Forum",
        actions = {
            IconButton(onClick = {
                forumViewModel.refreshForumPosts()
            }) {
                Icon(
                    painterResource(R.drawable.baseline_refresh_24),
                    contentDescription = "Refresh"
                )
            }
            IconButton(onClick = {
                onNavigateAddPost()
            }) {
                Icon(
                    painterResource(R.drawable.baseline_add_24),
                    contentDescription = "add post"
                )
            }
        }
    )
}