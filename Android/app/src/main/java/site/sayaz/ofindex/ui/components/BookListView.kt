package site.sayaz.ofindex.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import site.sayaz.ofindex.data.model.Book

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun BookListView(
    books: List<Book>,
    onBookClick: (Book) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3), // 每行三个项目
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(books) { book ->
            BookView(
                book = book,
                onBookClick = onBookClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 0.dp, max = Dp.Infinity)
                    .aspectRatio(2 / 3f) // 保持2:3的宽高比
            )
        }
    }
}

@Preview(fontScale = 1.0f, device = "spec:width=1080px,height=2340px,dpi=440",
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
    wallpaper = Wallpapers.NONE
)
@Composable
fun BookListViewPreview() {
    val books = listOf(
        Book(
            bookId = 1,
            name = "The Great Gatsby",
            author = "F. Scott Fitzgerald",
            description = "Set in the Jazz Age on Long Island, this novel explores themes of decadence, idealism, resistance to change, and social upheaval.",
            cover = "https://via.placeholder.com/150x200",
            tag = "Classic Literature",
            isbn = "9780743273565",
            bookClass = 1
        ),
        Book(
            bookId = 2,
            name = "To Kill a Mockingbird",
            author = "Harper Lee",
            description = "A novel that deeply explores serious issues of racial injustice and moral growth through the eyes of a young girl in the South during the 1930s.",
            cover = "https://via.placeholder.com/150",
            tag = "American Literature",
            isbn = "9780061947416",
            bookClass = 2
        ),
        Book(
            bookId = 3,
            name = "1984",
            author = "George Orwell",
            description = "A dystopian novel about the struggles of a man under the thumb of a totalitarian regime, where truth and individuality are constantly under attack.",
            cover = "https://via.placeholder.com/155",
            tag = "Dystopian Fiction",
            isbn = "9780451524935",
            bookClass = 3
        ),
        Book(
            bookId = 4,
            name = "Pride and Prejudice",
            author = "Jane Austen",
            description = "A romantic novel of manners that follows the character development of Elizabeth Bennet, the dynamic protagonist, who learns about the repercussions of hasty judgments and comes to appreciate the difference between superficial goodness and actual goodness.",
            cover = "https://via.placeholder.com/1500",
            tag = "Romantic Fiction",
            isbn = "9780141439518",
            bookClass = 4
        ),
        Book(
            bookId = 5,
            name = "The Catcher in the Rye",
            author = "J.D. Salinger",
            description = "An influential novel that deals with complex issues faced by a teenager as he searches for truth and meaning in an 'phony' adult world.",
            cover = "https://via.placeholder.com/15",
            tag = "Young Adult Fiction",
            isbn = "9780316769488",
            bookClass = 5
        )
    )
    BookListView(
        books = books,
        onBookClick = {},
    )
}
