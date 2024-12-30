package site.sayaz.ofindex.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import site.sayaz.ofindex.R
import site.sayaz.ofindex.data.model.Pack
import site.sayaz.ofindex.ui.theme.Typography




@Composable
fun PackView(
    pack: Pack,
    onLikeClick: () -> Unit = {},
    onAddClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 作者头像
        Image(
            painter = rememberAsyncImagePainter(
                model = pack.authorAvatar,
                placeholder = painterResource(id = R.drawable.outline_account_circle_24_grey),
                error = painterResource(id = R.drawable.outline_account_circle_24_grey)
            ),
            contentDescription = "Author Avatar",
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(24.dp)), // 圆形头像，
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))

        // 包名
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = pack.name?:"",
                style = Typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = pack.description?:"",
                style = Typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        val isLike = remember { mutableStateOf(pack.liked?:false) }
        val likeIcon = if (isLike.value) R.drawable.baseline_favorite_24 else R.drawable.baseline_favorite_border_24
        // 点赞键
        IconButton(onClick = {
            onLikeClick()
            isLike.value = true
        }) {
            Icon(
                painter = painterResource(likeIcon),
                contentDescription = "Like"
            )
        }
        Spacer(modifier = Modifier.width(8.dp))

        // 订阅键
        IconButton(onClick = onAddClick) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_add_24),
                contentDescription = "Subscribe",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(backgroundColor = 0xFFFFFF, showBackground = true)
@Composable
fun PackViewPreview() {
    PackView(
        Pack(
            authorId = 1,
            authorAvatar = "https://via.placeholder.com/150",
            name = "Pack Name",
            packId = 1,
            description = "desdesdesdesdesdesdesdesdesdesdesdesdesdesdesdesdesdesdes"
        )
    )
}