package site.sayaz.ofindex.ui.screen.forum


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import site.sayaz.ofindex.R
import site.sayaz.ofindex.data.model.Comment
import site.sayaz.ofindex.data.model.CommentComment
import site.sayaz.ofindex.data.model.Pack
import site.sayaz.ofindex.ui.theme.Typography

@Composable
fun CommentItem(
    comment: Comment,
    onToggleReply: (Comment) -> Unit,
    isExpanded: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 作者头像和名称
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = comment.userAvatar,
                        placeholder = painterResource(id = R.drawable.outline_account_circle_24),
                        error = painterResource(id = R.drawable.outline_account_circle_24)),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = comment.userName.orEmpty(),
                        style = Typography.titleSmall
                    )
                    Text(
                        text = "Likes: ${comment.likes ?: 0}",
                        style = Typography.bodySmall
                    )
                }
            }

            // 评论内容
            Text(
                text = comment.text.orEmpty(),
                style = Typography.bodyMedium
            )

            // 楼中楼按钮
            if (comment.comments?.isNotEmpty() == true) {
                Button(
                    onClick = { onToggleReply(comment) },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = if (isExpanded) "Hide Replies" else "Show Replies (${comment.comments.size})"
                    )
                }

                // 展示楼中楼
                if (isExpanded) {
                    comment.comments.forEach { reply ->
                        ReplyItem(reply, modifier = Modifier.padding(start = 16.dp))
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun CommentItemPreview(){
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
