package site.sayaz.ofindex.ui.screen.shelf
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontSynthesis.Companion.Style
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.serialization.json.Json
import site.sayaz.ofindex.data.model.NodeData
import site.sayaz.ofindex.data.model.PackContent
import kotlin.math.max



@Composable
fun ReadMindMapContent(content: PackContent, modifier: Modifier = Modifier) {
    val diagram = content.diagram
    var offset by remember { mutableStateOf(Offset.Zero) }
    var zoom by remember { mutableStateOf(1f) }

    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .transformable(
                    state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
                        offset -= offsetChange
                        zoom *= zoomChange
                        // Ignore rotation for simplicity
                    }
                )
        ) {
            // 应用缩放和偏移量
            withTransform({
                scale(scale = zoom)
                translate(left = -offset.x, top = -offset.y)
            }) {
                drawDiagram(diagram.nodeData, Offset(size.width / 2, 50f), zoom, 0)
            }
        }
    }
}

private fun DrawScope.drawDiagram(
    node: NodeData,
    position: Offset,
    zoom: Float,
    level: Int
) {
    // 计算节点的大小，并应用缩放
    val nodeWidth = max(node.topic.length * 15, 100).toFloat() * zoom
    val nodeHeight = 50F * zoom
    val nodeSize = Size(nodeWidth, nodeHeight)

    // 绘制节点背景
    drawRoundRect(
        color = Color.Gray,
        topLeft = position - Offset(nodeSize.width / 2, nodeSize.height / 2),
        size = nodeSize,
        cornerRadius = CornerRadius(100f * zoom, 100f * zoom)
    )

    // 绘制节点文本
    drawContext.canvas.nativeCanvas.drawText(
        node.topic,
        position.x,
        position.y + 10 * zoom, // 调整文本的位置以适应缩放
        android.graphics.Paint().apply {
            color = android.graphics.Color.WHITE
            textSize = 20f * zoom // 调整字体大小以适应缩放
            textAlign = android.graphics.Paint.Align.CENTER
            style = android.graphics.Paint.Style.FILL
            isFakeBoldText = true
        }
    )

    if (!node.children.isNullOrEmpty()) {
        val childCount = node.children.size
        val verticalSpacing = 80f * zoom
        val totalHeight = (childCount - 1) * verticalSpacing + nodeSize.height

        for ((index, child) in node.children.withIndex()) {
            val childPosition = Offset(
                position.x + nodeSize.width / 2 + 100f * zoom,
                position.y - totalHeight / 2 + index * verticalSpacing + nodeSize.height / 2
            )

            // 绘制带弧度的连接线
            drawCurvedLine(position + Offset(nodeSize.width / 2, 0f), childPosition - Offset(nodeSize.width / 2, 0f), zoom)

            // 递归绘制子节点
            drawDiagram(child, childPosition, zoom, level + 1)
        }
    }
}

// 使用二次贝塞尔曲线绘制带弧度的连接线，并应用缩放
private fun DrawScope.drawCurvedLine(start: Offset, end: Offset, zoom: Float) {
    val controlPoint = Offset(
        (start.x + end.x) / 2, // 控制点的x坐标为起点和终点x坐标的平均值
        end.y // 控制点的y坐标与终点相同，或者可以调整以改变弧度
    )

    // 创建路径并添加贝塞尔曲线
    val path = Path().apply {
        moveTo(start.x, start.y)
        quadraticTo(controlPoint.x, controlPoint.y, end.x, end.y)
    }

    // 绘制路径，并应用缩放到线条宽度
    drawPath(
        path = path,
        color = Color.Black,
        style = Stroke(width = 2f * zoom) // 调整线条宽度以适应缩放
    )
}

@Preview
@Composable
fun MindMapPreview() {
    val content =
        "{\"note\":[{\"id\":\"67dc8bae-4343-4de7-8fd8-268574eed106\",\"name\":\"1111\",\"rect\":null,\"children\":[]},{\"id\":\"cbd6e4ce-a5e8-4ccb-90d2-ec580717fbac\",\"name\":\"1111\",\"rect\":null,\"children\":[]}],\"diagram\":{\"nodeData\":{\"id\":\"c9ee977189f3b1f1\",\"topic\":\"Root\",\"root\":true,\"children\":[{\"id\":\"c9ee977189f3b1f1\",\"topic\":\"c1\",\"root\":true,\"children\":[{\"id\":\"c9ee977189f3b1f1\",\"topic\":\"cc1\",\"root\":true,\"children\":[]}]},{\"id\":\"c9ee977189f3b1f1\",\"topic\":\"c2\",\"root\":true,\"children\":[]},{\"id\":\"c9ee977189f3b1f1\",\"topic\":\"c3\",\"root\":true,\"children\":[]}]}}}"
    val packContent = Json.decodeFromString<PackContent>(content)
    ReadMindMapContent(packContent)
}