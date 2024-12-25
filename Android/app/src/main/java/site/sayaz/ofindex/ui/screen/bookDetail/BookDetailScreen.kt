package site.sayaz.ofindex.ui.screen.bookDetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import site.sayaz.ofindex.data.model.Pack
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
    val isLiked by bookDetailViewModel.isLiked.collectAsState()
    val shelfList by bookDetailViewModel.shelfList.collectAsState()
    val bookInShelfList by bookDetailViewModel.bookInShelfList.collectAsState()
    val userPackList by bookDetailViewModel.userPackList.collectAsState()
    val chosenPack by bookDetailViewModel.chosenPack.collectAsState()


    val likeListExpanded = remember { mutableStateOf(false) }
    val unLikeListExpanded = remember { mutableStateOf(false) }
    val packListExpanded = remember { mutableStateOf(false) }

    LaunchedEffect(bookId) {
        bookDetailViewModel.getBookDetail(bookId)
        bookDetailViewModel.getPackList(bookId)
        bookDetailViewModel.findBook(bookId)
        bookDetailViewModel.getUserPackList(bookId)
    }
    LaunchedEffect(Unit) { bookDetailViewModel.getSimpleShelf() }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { book.name },
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
                Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
                    ButtonWithLabel(
                        iconResId = R.drawable.baseline_favorite_border_24,
                        label = "add to shelf",
                        onClick = { likeListExpanded.value = true }
                    )
                    DropdownMenu(
                        expanded = likeListExpanded.value,
                        onDismissRequest = { likeListExpanded.value = false }) {
                        shelfList.forEach { shelf ->
                            DropdownMenuItem(
                                text = { Text(shelf.name) },
                                onClick = {
                                    bookDetailViewModel.addBook(bookId, shelf.booklistID)
                                    likeListExpanded.value = false
                                    unLikeListExpanded.value = false
                                }
                            )
                        }
                    }
                }
                Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
                    ButtonWithLabel(
                        iconResId = R.drawable.baseline_favorite_24,
                        label = "remove from shelf",
                        onClick = { unLikeListExpanded.value = true }
                    )
                    DropdownMenu(
                        expanded = unLikeListExpanded.value,
                        onDismissRequest = { unLikeListExpanded.value = false }) {
                        bookInShelfList.forEach { shelf ->
                            DropdownMenuItem(
                                text = { Text(shelf.name) },
                                onClick = {
                                    bookDetailViewModel.removeBook(bookId, shelf.booklistID)
                                    likeListExpanded.value = false
                                    unLikeListExpanded.value = false
                                }
                            )
                        }
                    }
                }
                Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
                    ButtonWithLabel(
                        iconResId = R.drawable.package_2_24px,
                        label = if (chosenPack == null) "choose pack" else chosenPack!!.name
                            ?: "choose pack",
                        onClick = { packListExpanded.value = true }
                    )
                    DropdownMenu(
                        expanded = packListExpanded.value,
                        onDismissRequest = { packListExpanded.value = false }) {
                        userPackList.forEach { pack ->
                            DropdownMenuItem(
                                text = { Text(pack.packName ?: "packname") },
                                onClick = {
                                    bookDetailViewModel.choosePack(
                                        Pack(
                                            packId = pack.packID,
                                            name = pack.packName
                                        )
                                    )
                                    packListExpanded.value = false
                                }
                            )
                        }


                    }


                }


            }
            Spacer(modifier = Modifier.height(16.dp))
            if (packList.isNotEmpty()) {
                LazyColumn {
                    items(packList) { pack ->
                        PackView(pack,
                            onLikeClick = {
                                bookDetailViewModel.likePack(pack.packId ?: -1)
                            },
                            onAddClick = {
                                bookDetailViewModel.addPack(pack.packId ?: -1)
                            })
                    }
                }
            }

        }
    }

}

