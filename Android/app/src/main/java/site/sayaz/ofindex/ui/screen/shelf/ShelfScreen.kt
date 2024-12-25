package site.sayaz.ofindex.ui.screen.shelf

import androidx.annotation.RequiresPermission
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import site.sayaz.ofindex.data.model.Book
import site.sayaz.ofindex.ui.components.BookListView
import site.sayaz.ofindex.ui.components.PDFView
import site.sayaz.ofindex.viewmodel.ShelfViewModel
import site.sayaz.ofindex.ui.theme.Typography

@Composable
fun ShelfScreen(
    shelfViewModel: ShelfViewModel,
    onNavigateRead: (Long) -> Unit,
    onNavigateBookDetail: (Long) -> Unit
) {
    val bookLists by shelfViewModel.bookListList.collectAsState()

    LaunchedEffect(Unit) {
        shelfViewModel.getShelf()
    }
    Box(modifier = Modifier.fillMaxSize()) {


        LazyColumn(modifier = Modifier.fillMaxSize()) {
            // 默认书单直接显示在书架页
            val defaultBooks = bookLists.find { it.name == "Default" }?.books.orEmpty()
            if (defaultBooks.isNotEmpty()) {
                item {
                    ShelfBookListView(
                        books = defaultBooks,
                        onBookClick = onNavigateBookDetail
                    )
                }
            }

            // 显示其他书单
            val otherBookLists = bookLists.filterNot { it.name == "Default" }
            if (otherBookLists.isNotEmpty()) {
                otherBookLists.forEach { bookList ->
                    item {
                        Text(
                            text = bookList.name,
                            style = Typography.titleMedium,
                            modifier = Modifier.padding(horizontal = 12.dp,vertical = 4.dp)
                        )
                        ShelfBookListView(
                            books = bookList.books,
                            onBookClick = onNavigateBookDetail
                        )
                    }
                }
            }
        }
    }
}