package site.sayaz.ofindex.ui.screen.reader

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import site.sayaz.ofindex.viewmodel.ReaderViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.unit.dp
import site.sayaz.ofindex.ui.components.PDFView


@Composable
fun ReaderScreen(viewModel: ReaderViewModel) {
    val books by viewModel.books.collectAsState()
    val pdfContent by viewModel.pdfContent.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        if (books.isEmpty()) {
            Text("Loading books...", Modifier.fillMaxSize(), textAlign = TextAlign.Center)
        } else {
            LazyColumn {
                items(books) { book ->
                    Text(
                        book.title,
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable { viewModel.loadPdfContent(book.id) }
                    )
                }
            }
        }

        pdfContent?.let {
            PDFView(modifier = Modifier.fillMaxSize(), pdfBytes = it)
        }
    }
}