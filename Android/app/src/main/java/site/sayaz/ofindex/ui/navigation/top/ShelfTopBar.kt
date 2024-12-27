package site.sayaz.ofindex.ui.navigation.top

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import site.sayaz.ofindex.R
import site.sayaz.ofindex.ui.components.BaseTopBar
import site.sayaz.ofindex.ui.screen.shelf.CreateBookListForm
import site.sayaz.ofindex.viewmodel.ShelfViewModel
import site.sayaz.ofindex.ui.theme.Typography

@Composable
fun ShelfTopBar(
    shelfViewModel: ShelfViewModel,
) {
    var expanded by remember { mutableStateOf(false) }
    var bookListName by remember { mutableStateOf("") }
    val density = LocalDensity.current
    val menuWidth = 300.dp // 设置下拉菜单的宽度
    val iconButtonSize = 48.dp // IconButton 的默认大小

    BaseTopBar(
        title = "Shelf",
        actions = {
            IconButton(onClick = { expanded = true }) {
                Icon(painterResource(R.drawable.baseline_add_24), contentDescription = "Add Book List")
            }
        }
    )
    val offset = DpOffset(
        x = (LocalConfiguration.current.screenWidthDp.dp - menuWidth - iconButtonSize),
        y = 0.dp
    )
    // 弹出创建书单的表单
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        offset = offset,
        modifier = Modifier
            .width(menuWidth) // 设置下拉菜单的宽度
            .padding(16.dp) // 添加内边距
            .fillMaxWidth()
    ) {
        CreateBookListForm(
            bookListName = bookListName,
            onNameChange = { newText -> bookListName = newText },
            onConfirm = {
                if (bookListName.isNotBlank()) {
                    shelfViewModel.createBookList(bookListName)
                    expanded = false
                }
            },
            onDismiss = { expanded = false }
        )
    }
}


