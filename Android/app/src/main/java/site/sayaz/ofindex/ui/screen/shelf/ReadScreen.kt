package site.sayaz.ofindex.ui.screen.shelf

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import site.sayaz.ofindex.R
import site.sayaz.ofindex.data.remote.RetrofitInstance
import site.sayaz.ofindex.ui.components.PDFView
import site.sayaz.ofindex.ui.components.Loading
import site.sayaz.ofindex.viewmodel.ReadViewModel


@Composable
fun ReadScreen(
    readViewModel: ReadViewModel,
    bookID: Long,
    onNavigateBack: () -> Unit
) {
    val pdfBytes by readViewModel.pdfBytes.collectAsState()
    val downloadProgress by RetrofitInstance.progress.collectAsState()
    val isBottomBarVisible by readViewModel.isBottomBarVisible.collectAsState()
    val isTopBarVisible by readViewModel.isTopBarVisible.collectAsState()
    val selectedTab by readViewModel.selectedTab.collectAsState()
    var pageCount by remember { mutableIntStateOf(1) }
    val pagerState = rememberPagerState(pageCount = { pageCount })

    LaunchedEffect(bookID) {
        readViewModel.loadBook(bookID)
    }

    Scaffold(
        topBar = {
            AnimatedVisibility(
                visible = isTopBarVisible,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                ReadTopBar(
                    onNavigateBack = onNavigateBack,
                    onToggleBottomBar = {visible ->
                        readViewModel.toggleBottomBarVisibility(visible)
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {}
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .clickable (
                    indication = null, // 禁用默认的点击指示
                    interactionSource = remember { MutableInteractionSource() } // 禁用交互源
                ){ readViewModel.toggleTopBarVisibility(!isTopBarVisible) }, // 点击屏幕切换顶部栏可见性
            contentAlignment = Alignment.Center,

        ) {
            if (pdfBytes != null) {
                HorizontalPager(state = pagerState) { page ->
                    PDFView(
                        modifier = Modifier.fillMaxSize(),
                        pdfBytes = pdfBytes!!,
                        page = page,
                        setPageCount = {
                            pageCount = it
                        }
                    )
                }
            } else {
                CircularProgressIndicator(
                    progress = { downloadProgress.div(1f) },
                    modifier = Modifier.size(50.dp),
                    trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
                )
            }
        }
        if(isBottomBarVisible){
            ReadBottomSheet(
                selectedTab = selectedTab,
                onDismissRequest = {
                    readViewModel.toggleBottomBarVisibility(false)
                },
                onTabSelected = {
                    readViewModel.selectTab(it)
                }
            )
        }

    }
}

