package site.sayaz.ofindex.ui.screen.forum

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import site.sayaz.ofindex.R
import site.sayaz.ofindex.data.model.Pack
import site.sayaz.ofindex.data.model.Post
import site.sayaz.ofindex.ui.components.TagView
import site.sayaz.ofindex.ui.theme.Typography

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PostDetail(
    post: Post,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(verticalAlignment = Alignment.Top) {
                // 作者头像
                Image(
                    painter = rememberAsyncImagePainter(
                        model = post.posterAvatar,
                        placeholder = painterResource(id = R.drawable.outline_account_circle_24),
                        error = painterResource(id = R.drawable.outline_account_circle_24)
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    // 作者ID 和 帖子名称
                    Text(
                        text = post.posterName ?: "",
                        style = Typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = post.title ?: "error",
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            // 描述
            Text(
                text = post.text ?: "",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))

            //tag
            FlowRow {
                post.tags.forEach { tag ->
                    TagView(tag)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            // pic
            if (post.images.isNotEmpty()) {
                LazyRow(
                    Modifier
                        .scrollable(
                            state = rememberScrollState(),
                            orientation = Orientation.Horizontal
                        )
                        .height(150.dp)
                        .fillMaxWidth()
                ) {
                    items(post.images) { imageUrl ->
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(4.dp)
                                .width(100.dp)
                        ) {
                            //Log.d("PostItem",imageUrl)
                            val imagePainter = rememberAsyncImagePainter(
                                model = imageUrl,
                                placeholder = painterResource(id = R.drawable.baseline_hide_image_4),
                                error = painterResource(id = R.drawable.baseline_hide_image_4)
                            )
                            Image(
                                painter = imagePainter,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize()
                            )
                            // 显示加载圈
                            val imageState = imagePainter.state.collectAsState()
                            if (imageState.value is AsyncImagePainter.State.Loading) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .size(32.dp)
                                )
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
fun PostDetailPreview() {
    val post = Post(
        postId = 1,
        posterAvatar = "https://example.com/avatar.jpg",
        posterName = "John Doe",
    )
    PostDetail(post = post)

}