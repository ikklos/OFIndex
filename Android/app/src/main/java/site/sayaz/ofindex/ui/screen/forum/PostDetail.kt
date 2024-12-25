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
import site.sayaz.ofindex.data.model.Pack
import site.sayaz.ofindex.data.model.Post
import site.sayaz.ofindex.ui.theme.Typography

@Composable
fun PostDetail(
    post: Post,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
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
                    painter = rememberAsyncImagePainter(
                        model = post.posterAvatar,
                        placeholder = painterResource(id = R.drawable.outline_account_circle_24),
                        error = painterResource(id = R.drawable.outline_account_circle_24)
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = post.posterName?:"no author name",
                    style = Typography.titleMedium
                )
            }

            // 帖子描述
            Text(
                text = "no description",
                style = Typography.bodyLarge,
                maxLines = 50,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview
@Composable
fun PostDetailPreview() {
    val post = Post(
        postId = 1,
        posterAvatar = "https://example.com/avatar.jpg",
        posterName = "John Doe",
    )
    PostDetail(post = post)

}