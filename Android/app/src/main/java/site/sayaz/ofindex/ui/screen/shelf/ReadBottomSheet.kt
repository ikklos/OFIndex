package site.sayaz.ofindex.ui.screen.shelf

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import site.sayaz.ofindex.viewmodel.ReadViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadBottomSheet(
    selectedTab: Int,
    onDismissRequest: () -> Unit,
    onTabSelected: (Int) -> Unit
){
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val tabList = listOf("Notes", "Mind Map")
    ModalBottomSheet(
        onDismissRequest = {
            onDismissRequest()
        },
        sheetState = sheetState,
    ) {
        TabRow(selectedTabIndex = selectedTab) {
            tabList.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { onTabSelected(index) },
                    text = { Text(title) }
                )
            }
        }

        // 根据选中的 Tab 显示不同的内容
        when (selectedTab) {
            0 -> ReadNotesContent()
            1 -> ReadMindMapContent()
        }
    }
}

@Preview
@Composable
fun ReadBottomSheetPreview() {
    val selectedTab = remember { mutableIntStateOf(1)}
    ReadBottomSheet(
        selectedTab.intValue,
        onDismissRequest = {},
        onTabSelected = { int -> selectedTab.intValue = int },
    )
}

