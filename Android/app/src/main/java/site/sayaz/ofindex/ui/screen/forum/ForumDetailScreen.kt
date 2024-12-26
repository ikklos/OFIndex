package site.sayaz.ofindex.ui.screen.forum

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import site.sayaz.ofindex.R
import site.sayaz.ofindex.viewmodel.ForumDetailViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumDetailScreen(
    forumDetailViewModel: ForumDetailViewModel,
    postID: Long,
    onNavigateBack: () -> Unit
) {
    val postDetail by forumDetailViewModel.forumDetail.collectAsState(initial = null)
    val comments by forumDetailViewModel.forumComments.collectAsState(initial = emptyList())

    val listState = rememberLazyListState()

    // 获取帖子详情和评论
    LaunchedEffect(postID) {
        forumDetailViewModel.getPostDetail(postID)
        forumDetailViewModel.getPostComments(postID)
    }

    var commentText by remember { mutableStateOf("") }
    var replyToUser by remember { mutableStateOf<Pair<String, Long>?>(null) } // 存储回复的目标用户信息
    val focusRequester = remember { FocusRequester() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = {
                        onNavigateBack()
                    }) {
                        Icon(
                            painterResource(R.drawable.baseline_arrow_back_24),
                            contentDescription = null
                        )
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier.padding(8.dp)
            ) {
                TextField(
                    value = commentText,
                    onValueChange = { newText ->
                        commentText = newText
                    },
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester), // 设置焦点请求器
                    placeholder = {
                        Text(
                            text = if (replyToUser != null) "回复 ${replyToUser?.first}：" else "输入评论",
                            color = Color.Gray
                        )
                    },
                    singleLine = true,
                    maxLines = 1,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                TextButton(
                    onClick = {
                        if (commentText.isNotBlank()) {
                            forumDetailViewModel.addComment(postID, commentText, replyToUser?.second)
                            commentText = ""
                            replyToUser = null // 发送后清除回复目标
                        }
                    },
                    enabled = commentText.isNotBlank(),
                    modifier = Modifier.padding(0.dp)
                ) {
                    Text("发送", modifier = Modifier.padding(0.dp))
                }
            }
        }
    ) { innerPadding ->
        postDetail?.let { post ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {

                LazyColumn(
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        PostDetail(post)
                        HorizontalDivider(thickness = 4.dp)
                    }
                    items(comments) { comment ->
                        val isExpanded = remember { mutableStateOf(false) }
                        CommentItem(
                            comment = comment,
                            onToggleReply = { isExpanded.value = !isExpanded.value },
                            isExpanded = isExpanded.value,
                            onCommentFocus = { name, id ->
                                replyToUser = Pair(name, id)
                                focusRequester.requestFocus() // 请求焦点
                            },
                            onLike = {
                                forumDetailViewModel.likeComment(it)
                            }
                        )
                        HorizontalDivider(thickness = 1.dp)
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
}