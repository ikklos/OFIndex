package site.sayaz.ofindex.ui.components

import android.widget.StackView
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import site.sayaz.ofindex.R
import site.sayaz.ofindex.data.model.Book


@Composable
fun BookView(
    book: Book,
    onBookClick: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(4.dp),
        shape = RoundedCornerShape(8.dp),
        onClick = { onBookClick(book) },
        ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
        ) {
            // 书封面
            Image(
                painter = rememberAsyncImagePainter(
                    model = book.cover,
                    placeholder = painterResource(id = R.drawable.ic_action_no_image), // 加载中显示的占位符
                    error = painterResource(id = R.drawable.ic_action_no_image) // 加载失败时显示的图片
                ),
                contentDescription = null, // 可选：提供内容描述
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // 书名，在封面的左下角
            Text(
                text = book.name?:"null",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 2,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(4.dp)
            )
        }

    }
}


@Preview
@Composable
fun BookViewPreview() {
    val book = Book(
        bookId = 1,
        name = "bookname",
        author = "author",
        description = "desp",
        cover = "https://via.placeholder.com/150",
        tag = "TODO()",
        isbn = "TODO()",
        bookClass = 1,
    )
    Column {
        BookView(
            book, {}, Modifier
                .width(150.dp)
                .height(300.dp)
        )
        BookView(
            book, {}, Modifier
                .width(300.dp)
                .height(100.dp)
        )
        BookView(
            book, {}, Modifier
                .width(100.dp)
                .height(100.dp)
        )
        BookView(
            book, {}, Modifier
                .width(300.dp)
                .height(300.dp)
        )
    }

}
