package site.sayaz.ofindex.ui.screen.shelf

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.serialization.json.Json
import site.sayaz.ofindex.data.model.Pack
import site.sayaz.ofindex.data.model.PackContent
import site.sayaz.ofindex.util.parseNotesContent
import site.sayaz.ofindex.viewmodel.ReadViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadBottomSheet(
    selectedTab: Int,
    onDismissRequest: () -> Unit,
    onTabSelected: (Int) -> Unit,
    pack: Pack = Pack()
){
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val tabList = listOf("Notes", "Mind Map")
    val packContent = remember { parseNotesContent(pack.content?:"") }


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
        if (packContent != null){
            when (selectedTab) {
                0 -> ReadNotesContent(packContent)
                1 -> ReadMindMapContent(packContent)
            }
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

