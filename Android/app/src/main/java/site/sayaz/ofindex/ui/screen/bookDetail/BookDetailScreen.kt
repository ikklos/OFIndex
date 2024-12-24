package site.sayaz.ofindex.ui.screen.bookDetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import site.sayaz.ofindex.R
import site.sayaz.ofindex.ui.components.BookView
import site.sayaz.ofindex.ui.components.ButtonWithLabel
import site.sayaz.ofindex.ui.components.PackView
import site.sayaz.ofindex.ui.theme.Typography
import site.sayaz.ofindex.viewmodel.BookDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    bookDetailViewModel: BookDetailViewModel,
    bookId: Long,
    onNavigateRead: (Long) -> Unit,
    onNavigateBack: () -> Unit
) {
    val book by bookDetailViewModel.bookDetail.collectAsState()
    val packList by bookDetailViewModel.packList.collectAsState()
    LaunchedEffect(Unit) {
        bookDetailViewModel.getBookDetail(bookId)
        bookDetailViewModel.getPackList(bookId)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},//no title
                navigationIcon = {
                    IconButton(onClick = {
                        onNavigateBack()
                    }) {
                        Icon(
                            painterResource(R.drawable.baseline_arrow_back_24),
                            contentDescription = "返回"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = {
                onNavigateRead(bookId)
            }) {
                Icon(
                    painterResource(R.drawable.baseline_arrow_right_24),
                    contentDescription = "开始阅读"
                )
                Text("Start Read")
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            BookIntroduction(book)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ButtonWithLabel(
                    iconResId = R.drawable.baseline_favorite_border_24,
                    label = "add to shelf",
                    onClick = { /* Handle add to shelf click */ }
                )
                ButtonWithLabel(
                    iconResId = R.drawable.package_2_24px,
                    label = "choose pack",
                    onClick = { /* Handle choose pack click */ }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(packList.size) { index ->
                    PackView(packList[index],
                        onLikeClick = {
                            bookDetailViewModel.likePack(packList[index].packId)
                        },
                        onAddClick = {
                            bookDetailViewModel.addPack(packList[index].packId)
                        })
                }
            }
        }
    }

}

