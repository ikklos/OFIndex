package site.sayaz.ofindex.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
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
    onBookClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    var boxHeight by remember { mutableStateOf(0) }
    Card(
        modifier = modifier.padding(4.dp),
        shape = RoundedCornerShape(8.dp),
        onClick = { onBookClick(book.bookId) },
        ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
                .onSizeChanged { size ->
                    boxHeight = size.height
                }
        ) {
            // 书封面
            Image(
                painter = rememberAsyncImagePainter(
                    model = book.cover,
                    placeholder = painterResource(id = R.drawable.ic_action_no_image),
                    error = painterResource(id = R.drawable.ic_action_no_image)
                ),
                contentDescription = book.description,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // 渐变层
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Black, Color.Transparent),
                            startY = with(density) { (boxHeight / 3 * 60).toDp().value },
                            endY = 0f
                        )
                    )
                    .alpha(1f) // 可以调整透明度
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


@Preview(backgroundColor = 0xFFFFFF, showBackground = true)
@Composable
fun BookViewPreview() {
    val book = Book(
        bookId = 1,
        name = "bookname",
        author = "author",
        description = "desp",
        cover = "https://via.placeholder.com/150",
        tag = emptyList(),
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
