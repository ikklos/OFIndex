package site.sayaz.ofindex.ui.screen.forum

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import site.sayaz.ofindex.R
import site.sayaz.ofindex.data.model.Post
import site.sayaz.ofindex.ui.components.TagView
import site.sayaz.ofindex.viewmodel.ForumViewModel
import site.sayaz.ofindex.viewmodel.ImageUploadStatus
import site.sayaz.ofindex.viewmodel.getUri

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddPostScreen(
    forumViewModel: ForumViewModel,
    onNavigateBack: () -> Unit,
) {
    val context = LocalContext.current
    val postData by forumViewModel.postData.collectAsState()
    val uploadProgress by forumViewModel.imageUploadStatus.collectAsState()

    val navController = rememberNavController()
    val backStackEntry = remember(navController) { navController.currentBackStackEntry }

    // 图片选择器
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        forumViewModel.uploadImages(uris)
    }

    DisposableEffect(backStackEntry) {
        onDispose {
            // 当 AddPostScreen 从导航堆栈中移除时调用清理方法
            forumViewModel.clearAddPostScreen()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Post") },
                navigationIcon = {
                    IconButton(onClick = {
                        onNavigateBack()
                        forumViewModel.clearAddPostScreen()
                    }) {
                        Icon(
                            painterResource(id = R.drawable.baseline_arrow_back_24),
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    // 提交按钮
                    IconButton(
                        onClick = {
                            forumViewModel.addPost(postData)
                            onNavigateBack()
                            forumViewModel.clearAddPostScreen()
                        },
                    ) {
                        Icon(
                            painterResource(id = R.drawable.baseline_check_24),
                            contentDescription = "Ok"
                        )
                    }
                }

            )

        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 标题输入框
                TextField(
                    value = postData.title ?: "",
                    onValueChange = { forumViewModel.updateTitle(it) },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                // 文本输入框
                TextField(
                    value = postData.description ?: "",
                    onValueChange = { forumViewModel.updateText(it) },
                    label = { Text("Text") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    maxLines = 5
                )

                // 图片预览
                if (uploadProgress.isNotEmpty()) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(uploadProgress) { url ->
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(8.dp)
                            ) {
                                Image(
                                    rememberAsyncImagePainter(
                                        model = url.getUri()
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                                if (url is ImageUploadStatus.Uploading ||
                                    url is ImageUploadStatus.Error
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }
                        }
                    }
                }

                // 标签输入
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val tag = remember { mutableStateOf("") }
                    TextField(
                        value = tag.value,
                        onValueChange = {
                            tag.value = it
                        },
                        label = { Text("Tag") },
                    )
                    IconButton(
                        onClick = {
                            forumViewModel.addTag(tag.value.trim())
                        }
                    ) {
                        Icon(
                            painterResource(R.drawable.baseline_add_24),
                            contentDescription = "add tag"
                        )
                    }
                }

                // 已添加的标签
                if (postData.tags.isNotEmpty()) {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        postData.tags.forEach { tag ->
                            TagView(tag = tag)
                        }
                    }
                }

                // 选择图片按钮
                Button(
                    onClick = { imagePicker.launch("image/*") },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Select Images")
                }


            }
        }
    }
}