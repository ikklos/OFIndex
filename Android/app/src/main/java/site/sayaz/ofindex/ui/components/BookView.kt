package site.sayaz.ofindex.ui.components

import android.widget.StackView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import site.sayaz.ofindex.R



@Composable
fun BookView(
    bookTitle: String,
    bookCoverImageUrl: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        // 使用 Box 布局来堆叠 Image 和 Text
        Box(
            modifier = modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
        ) {
            // 书封面
            Image(
                painter = rememberAsyncImagePainter(bookCoverImageUrl),
                contentDescription = null, // 可选：提供内容描述
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // 书名，在封面的左下角
            Text(
                text = bookTitle,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White,
                maxLines = 2,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
            )
        }

    }
}


@Preview
@Composable
fun BookViewPreview() {
    BookView(
        "ysssss",
        "http://10.194.189.228:3000/{id}/cover.jpg",
        Modifier
            .width(100.dp)
            .height(150.dp)
    )
}
