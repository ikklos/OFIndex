package site.sayaz.ofindex.ui.screen.forum

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import site.sayaz.ofindex.viewmodel.ForumDetailViewModel


@Composable
fun ForumDetailScreen(
    forumDetailViewModel: ForumDetailViewModel,
    postID: Long
) {
    val postDetail by forumDetailViewModel.forumDetail.collectAsState(initial = null)
    val comments by forumDetailViewModel.forumComments.collectAsState(initial = emptyList())

    val listState = rememberLazyListState()



    // 获取帖子详情和评论
    LaunchedEffect(postID) {
        forumDetailViewModel.getPostDetail(postID)
        forumDetailViewModel.getPostComments(postID)
    }

    // 展示帖子详情
    postDetail?.let { post ->
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            PostDetail(post = post)
            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(comments) { comment ->
                    val isExpanded = remember { mutableStateOf(false) }
                    CommentItem(
                        comment = comment,
                        onToggleReply = { isExpanded.value = !isExpanded.value },
                        isExpanded = isExpanded.value
                    )
                }
            }
        }
    } ?: run {
        // 如果帖子详情为空，显示加载中或错误提示
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}