package site.sayaz.ofindex.ui.screen.shelf

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import site.sayaz.ofindex.data.model.Book
import site.sayaz.ofindex.ui.components.BookView
import site.sayaz.ofindex.ui.theme.Typography


@Composable
fun ShelfBookListView(
    books: List<Book>,
    onBookClick: (Long) -> Unit,
    modifier: Modifier = Modifier
){
    // 定义每行显示的书籍数量
    val columns = 3

    // 计算需要多少行
    val rows = (books.size + columns - 1) / columns

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        for (rowIndex in 0 until rows) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                for (colIndex in 0 until columns) {
                    val index = rowIndex * columns + colIndex
                    if (index < books.size) {
                        BookView(
                            book = books[index],
                            onBookClick = onBookClick,
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(3 / 4f)
                        )
                    } else {
                        // 如果当前行未填满，则添加空占位符
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ShelfBookListViewPreview() {
    val books = List(10) { index ->
        Book(
            bookId = index.toLong(),
            name = "Book $index",
            author = "Author $index",
            description = "Description $index",
            cover = "https://via.placeholder.com/150",
            tag = emptyList(),
            isbn = "TODO()",
            bookClass = 1,
            addTime = "TODO()"
        )
    }
    Column {
        Text(
            text = "bookList.name",
            style = Typography.titleMedium,
            modifier = Modifier.padding(horizontal = 12.dp,vertical = 4.dp)
        )
        ShelfBookListView(books, {})

    }


}