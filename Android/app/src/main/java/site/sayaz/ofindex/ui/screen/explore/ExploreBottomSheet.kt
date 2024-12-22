package site.sayaz.ofindex.ui.screen.explore

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import site.sayaz.ofindex.data.model.Class
import site.sayaz.ofindex.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreBottomSheet(
    onDismiss: () -> Unit = {},
    classList: List<Class>,
    onClassSelected: (Long) -> Unit,
    initialSelectedClassId: Long,
    selectedClassId: Long = initialSelectedClassId
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Select Book Class",
                style = Typography.titleMedium,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                classList.forEach { bookClass ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .selectable(
                                selected = (bookClass.id == selectedClassId),
                                onClick = {
                                    onClassSelected(bookClass.id)
                                },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (bookClass.id == selectedClassId),
                            onClick = null
                        )
                        Text(
                            text = bookClass.name,
                            style = Typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun ExploreBottomSheetPreview() {
    ExploreBottomSheet(
        classList = listOf(
            Class(1, "小说"),
            Class(2, "科幻"),
            Class(3, "编程"),
            Class(4, "历史"),
            Class(5, "哲学")
        ),
        onClassSelected = { selectedClassId ->
            // 处理类别选择的逻辑
            println("Selected class ID: $selectedClassId")
        },
        initialSelectedClassId = 1 // 初始选中的类别 ID
    )
}