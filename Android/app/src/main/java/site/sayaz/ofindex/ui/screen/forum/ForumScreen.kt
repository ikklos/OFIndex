package site.sayaz.ofindex.ui.screen.forum

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import site.sayaz.ofindex.util.TODOScreen
import site.sayaz.ofindex.viewmodel.ForumViewModel

@Composable
fun ForumScreen(
    forumViewModel: ForumViewModel,
    onNavigateForumDetail: (Long) -> Unit
) {
    val posts = forumViewModel.forumPosts.collectAsState()

    LaunchedEffect(Unit) {
        forumViewModel.getForumPosts()
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(posts.value) { post ->
            PostItem(
                post = post,
                onNavigateForumDetail = onNavigateForumDetail
            )
        }

        item {
            if (forumViewModel.hasMore && !forumViewModel.isLoading) {
                LaunchedEffect(Unit) {
                    forumViewModel.getForumPosts()
                }
            }
        }
    }


}