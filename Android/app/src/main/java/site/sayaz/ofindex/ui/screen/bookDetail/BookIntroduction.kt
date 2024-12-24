package site.sayaz.ofindex.ui.screen.bookDetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import site.sayaz.ofindex.R
import site.sayaz.ofindex.data.model.Book
import site.sayaz.ofindex.ui.components.BookView
import site.sayaz.ofindex.ui.components.TagView
import site.sayaz.ofindex.ui.theme.Typography

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BookIntroduction(
    book: Book
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {

        Row(verticalAlignment = Alignment.Top) {
            // cover
            Image(
                rememberAsyncImagePainter(
                    model = book.cover,
                    placeholder = painterResource(id = R.drawable.ic_action_no_image),
                    error = painterResource(id = R.drawable.ic_action_no_image)
                ),
                contentDescription = book.description,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(100.dp)
                    .height(150.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.LightGray)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Spacer(modifier = Modifier.height(16.dp))
                //title
                Text(text = book.name ?: "", style = Typography.titleLarge, maxLines = 3)
                //author
                Row {
                    Icon(
                        painterResource(R.drawable.outline_person_24),
                        contentDescription = "author",
                        modifier = Modifier
                            .size(16.dp)
                            .align(Alignment.CenterVertically),
                        tint = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = book.author ?: "",
                        style = Typography.bodyMedium,
                        color = Color.DarkGray,
                        maxLines = 1
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "ISBN: ${book.isbn?:""}", style = Typography.bodySmall, color = Color.DarkGray)
            }

        }
        Spacer(modifier = Modifier.height(16.dp))
        // des
        var expanded by remember { mutableStateOf(false) }
        val icon = if (expanded) R.drawable.baseline_arrow_drop_up_24
            else R.drawable.baseline_arrow_drop_down_24
        val text = if (expanded) book.description else book.description?.take(100)
        Column(modifier = Modifier.clickable(
            indication = null, // 禁用默认的点击指示
            interactionSource = remember { MutableInteractionSource() }, // 禁用交互源
            onClick = { expanded = !expanded }
        ).fillMaxWidth()) {
            Text(
                text = text ?: "",
                maxLines = if (expanded) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis
            )
            Icon(
                painterResource(icon),
                contentDescription = "Expand",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Tags
        FlowRow {
            book.tag.forEach{tag ->
                TagView(tag)
            }
        }
    }
}

@Preview(
    backgroundColor = 0xFFFFFF, showBackground = true,
    device = "spec:width=411dp,height=891dp"
)
@Composable
fun BookIntroductionPreview() {
    Surface {
        BookIntroduction(
            Book(
                bookId = 1,
                name = "bookname",
                author = "author",
                description = "dsaads",
                cover = "https://via.placeholder.com/150",
                tag = listOf("tag1", "tag2"),
                isbn = "978-3-16-148410-0",
                bookClass = 1,
            )
        )
    }

}
