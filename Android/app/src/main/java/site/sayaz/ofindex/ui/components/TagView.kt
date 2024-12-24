package site.sayaz.ofindex.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TagView(
    tag: String,
    onTagClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .clickable(
                onClick = { onTagClick(tag) },
                indication = null, // 禁用默认的点击指示
                interactionSource = remember { MutableInteractionSource() } // 禁用交互源
            )
            .width(IntrinsicSize.Min), // 自适应宽度
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        border = BorderStroke(1.dp, Color.LightGray),
    ) {
        Box(
            modifier = Modifier.padding(8.dp).background(Color.Transparent),
            contentAlignment = Alignment.Center, // 文字居中
        ) {
            Text(
                text = tag,
                textAlign = TextAlign.Center, // 文字水平居中
                fontSize = 12.sp, // 设置字体大小
                maxLines = 1, // 限制为一行
                overflow = TextOverflow.Ellipsis, // 超出部分用省略号表示
            )
        }
    }
}

@Preview
@Composable
fun TagViewPreview(){
    TagView("tag")
}

