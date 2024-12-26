package site.sayaz.ofindex.ui.screen.forum

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import site.sayaz.ofindex.R
import site.sayaz.ofindex.data.model.CommentComment
import site.sayaz.ofindex.ui.theme.Typography

@Composable
fun ReplyItem(
    reply: CommentComment,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
//        Image(
//            painter = rememberAsyncImagePainter(model = reply.userAvatar,
//                placeholder = painterResource(id = R.drawable.outline_account_circle_24),
//                error = painterResource(id = R.drawable.outline_account_circle_24)
//            ),
//            contentDescription = null,
//            modifier = Modifier
//                .size(32.dp)
//                .clip(CircleShape)
//        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = reply.userName.orEmpty(),
                style = Typography.titleSmall
            )
            Text(
                text = reply.text.orEmpty(),
                style = Typography.bodyMedium
            )
        }
    }
}