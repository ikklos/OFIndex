package site.sayaz.ofindex.ui.screen.forum


import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import site.sayaz.ofindex.R
import site.sayaz.ofindex.data.model.Comment
import site.sayaz.ofindex.data.model.CommentComment
import site.sayaz.ofindex.ui.theme.Typography

@Composable
fun CommentItem(
    comment: Comment,
    onToggleReply: () -> Unit,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
    onCommentFocus: (String,Long) -> Unit  = { s: String, l: Long -> } ,// 用于聚焦评论条并设置回复用户ID
    onLike : (Long) -> Unit = {}
) {
    var isLiked by remember { mutableStateOf(false) }
    var likesCount by remember { mutableStateOf(comment.likes ?: 0) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .animateContentSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // 用户头像
            Image(
                painter = rememberAsyncImagePainter(
                    model = comment.userAvatar,
                    placeholder = painterResource(id = R.drawable.outline_account_circle_24),
                    error = painterResource(id = R.drawable.outline_account_circle_24)
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .align(Alignment.Top)
            )
            Spacer(modifier = Modifier.width(16.dp))

            // 用户名和评论内容
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = comment.userName.orEmpty(),
                    style = Typography.titleSmall
                )
                Spacer(modifier = Modifier.height(16.dp))

                // 评论内容
                Text(
                    text = comment.text.orEmpty(),
                    style = Typography.bodyLarge,
                )
                Spacer(modifier = Modifier.height(16.dp))

                // 点赞和其他交互元素
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            isLiked = true
                            if (isLiked) {
                                likesCount += 1
                            }
                            onLike(comment.commentId?:0)
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            painterResource(
                                id = if (isLiked) R.drawable.baseline_thumb_up_alt_24 else R.drawable.baseline_thumb_up_off_alt_24
                            ),
                            contentDescription = null
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("$likesCount")
                    Spacer(modifier = Modifier.width(12.dp))
                    IconButton(
                        onClick = {
                            onCommentFocus(comment.userName?:"",comment.commentId?:0) // 聚焦评论条并设置回复用户ID
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            painterResource(R.drawable.baseline_chat_bubble_outline_24),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 32.dp, top = 16.dp)
                ) {
                    if (comment.comments?.isNotEmpty() == true) {
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        ) {
                            // 显示默认数量或所有回复
                            val visibleReplies = if (isExpanded) {
                                comment.comments
                            } else {
                                comment.comments.take(2)
                            }

                            visibleReplies.forEachIndexed { index, reply ->
                                ReplyItem(
                                    reply = reply,
                                    modifier = Modifier.padding(
                                        start = 16.dp,
                                        bottom = if (index == visibleReplies.lastIndex) 0.dp else 8.dp
                                    )
                                )
                            }

                            // 如果有更多回复且未展开，则显示“查看更多”按钮
                            if (comment.comments.size > 2) {
                                IconButton(
                                    onClick = { onToggleReply() },
                                    modifier = Modifier
                                        .align(Alignment.End)
                                        .size(24.dp)
                                ) {
                                    Icon(
                                        painterResource(
                                            id = if (isExpanded) R.drawable.baseline_arrow_drop_up_24 else R.drawable.baseline_arrow_drop_down_24
                                        ),
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun CommentItemPreview() {
    val comment = Comment(
        commentId = 1,
        comments = listOf(
            CommentComment(
                commentId = 2,
                createTime = "2023-07-22 10:30:00",
                likes = 5,
                text = "Great post!",
                userAvatar = "https://example.com/avatar.jpg",
                userId = 1,
                userName = "John Doe"
            ),
            CommentComment(
                commentId = 3,
                createTime = "2023-07-22 11:45:00",
                likes = 2,
                text = "I agree with your point.",
                userAvatar = "https://example.com/avatar.jpg",
                userId = 2,
                userName = "Jane Smith"
            ),
            CommentComment(
                commentId = 3,
                createTime = "2023-07-22 11:45:00",
                likes = 2,
                text = "I agree with your point.",
                userAvatar = "https://example.com/avatar.jpg",
                userId = 2,
                userName = "Jane Smith"
            )
        ),
        count = 2,
        createTime = "2023-07-22 09:15:00",
        likes = 10,
        text = "This is a sample comment with replies.",

        userAvatar = "https://example.com/avatar.jpg",
        userId = 1,
        userName = "John Doe"
    )
    Column {
        CommentItem(comment = comment, onToggleReply = {}, isExpanded = false)
        CommentItem(comment = comment, onToggleReply = {}, isExpanded = true)
    }

}
