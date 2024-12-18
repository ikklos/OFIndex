package site.sayaz.ofindex.ui.screen.shelf

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import site.sayaz.ofindex.ui.components.PDFView
import site.sayaz.ofindex.viewmodel.ShelfViewModel
import site.sayaz.ofindex.ui.components.Loading

@Composable
fun ReadScreen(
    viewModel: ShelfViewModel,
    bookID: String
) {
    val context = LocalContext.current
    val pdfBytes = viewModel.pdfBytes.collectAsState(initial = null).value
    val pagerState = rememberPagerState (pageCount = {3})

    LaunchedEffect(Unit) {
        viewModel.loadPdf(context,bookID)
    }
    Box(Modifier
        .fillMaxSize()
    ){
        if (pdfBytes != null) {
            HorizontalPager(pagerState) {page->
                PDFView(
                    modifier = Modifier.fillMaxWidth(),
                    pdfBytes = pdfBytes,
                    page = page
                )
            }
        } else {
            // 显示加载状态
            Loading()
        }
    }

}