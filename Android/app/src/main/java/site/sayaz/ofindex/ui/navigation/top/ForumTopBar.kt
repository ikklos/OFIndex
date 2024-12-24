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
    forumViewModel: ForumViewModel
){
    BaseTopBar(
        title = "Forum",
        actions = {
            IconButton(onClick = {

            }) {
                Icon(
                    painterResource(R.drawable.baseline_refresh_24),
                    contentDescription = "Refresh"
                )
            }
        }
    )
}