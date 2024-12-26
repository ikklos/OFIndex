package site.sayaz.ofindex.ui.screen.shelf

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.serialization.json.Json
import site.sayaz.ofindex.R
import site.sayaz.ofindex.data.model.Note
import site.sayaz.ofindex.data.model.PackContent
import site.sayaz.ofindex.ui.theme.Typography

@Composable
fun ReadNotesContent(content: PackContent, onJumpClick: (Int) -> Unit = {}) {
    Log.d(TAG, "ReadNotesContent: $content")
    Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
        content.note.forEach { note ->
            ReadNodeItem(
                note,
                onJumpClick = onJumpClick
            )
        }
        if (content.note.isEmpty()) {
            Text(
                text = "No Notes",
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}


@Composable
fun ReadNodeItem(
    note: Note,
    onJumpClick: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Page: ${note.rect?.page}",
                style = Typography.bodySmall,
                color = Color.Gray
            )
            Text(
                text = note.name,
                modifier = Modifier.padding(bottom = 4.dp),
                style = Typography.bodyMedium
            )

        }
        IconButton(
            onClick = {
                onJumpClick(note.rect?.page ?: -1)
            },
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Icon(
                painterResource(R.drawable.baseline_arrow_forward_ios_24),
                contentDescription = "jump to page"
            )
        }
    }
    HorizontalDivider(Modifier.padding(4.dp))
}


@Preview(showBackground = true)
@Composable
fun ReadNotesContentPreview() {
    val content =
        "{\"note\":[{\"id\":\"67dc8bae-4343-4de7-8fd8-268574eed106\",\"name\":\"11saddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd11\",\"rect\":null,\"children\":[]},{\"id\":\"cbd6e4ce-a5e8-4ccb-90d2-ec580717fbac\",\"name\":\"1111\",\"rect\":null,\"children\":[]}],\"diagram\":{\"nodeData\":{\"id\":\"c9ee977189f3b1f1\",\"topic\":\"Root\",\"root\":true,\"children\":[]}}}"
    val packContent = Json.decodeFromString<PackContent>(content)
    ReadNotesContent(packContent)

}