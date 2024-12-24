package site.sayaz.ofindex.ui.screen.shelf

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import site.sayaz.ofindex.ui.components.PDFView
import site.sayaz.ofindex.ui.components.Loading
import site.sayaz.ofindex.viewmodel.ReadViewModel

@Composable
fun ReadScreen(
    readViewModel: ReadViewModel,
    bookID: Long
) {
    val context = LocalContext.current
    val pdfBytes by readViewModel.pdfBytes.collectAsState(initial = null)
    val downloadProgress by readViewModel.downloadProgress.collectAsState()
    val pagerState = rememberPagerState(pageCount = { 3 })

    LaunchedEffect(Unit) {
        readViewModel.loadBook(bookID)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (pdfBytes != null) {
            HorizontalPager(state = pagerState) { page ->
                PDFView(
                    modifier = Modifier.fillMaxWidth(),
                    pdfBytes = pdfBytes!!,
                    page = page
                )
            }
        } else {
            CircularProgressIndicator(
                progress = { downloadProgress?.toFloat()?.div(100f) ?: 0f },
                modifier = Modifier.size(50.dp),
                trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
            )

        }
    }
}