package site.sayaz.ofindex.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import site.sayaz.ofindex.ui.theme.Typography

@Composable
fun ButtonWithLabel(
    iconResId: Int,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        IconButton(onClick = onClick) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = label,
                tint = Color.DarkGray // 设置图标颜色为深灰色
            )
        }
//        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = Typography.bodyMedium.copy(
                color = Color.DarkGray, // 设置文本颜色为深灰色
                fontSize = 12.sp // 调整字体大小
            ),
            textAlign = TextAlign.Center
        )
    }
}
