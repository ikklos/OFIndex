package site.sayaz.ofindex.ui.components

import android.content.Context
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.widget.ImageView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import java.io.File

@Composable
fun PDFView(modifier: Modifier, pdfBytes: ByteArray) {
    AndroidView(
        modifier = modifier,
        factory = { context: Context ->
            // 创建 ImageView 作为 PDF 页面显示的容器
            ImageView(context).apply {
                val tempFile = File(context.cacheDir, "temp.pdf")
                tempFile.writeBytes(pdfBytes)
                val parcelFileDescriptor = ParcelFileDescriptor.open(tempFile, ParcelFileDescriptor.MODE_READ_ONLY)
                val pdfRenderer = PdfRenderer(parcelFileDescriptor)

                // 加载 PDF 的第一页（可以扩展为分页显示）
                pdfRenderer.openPage(0).use { page ->
                    val bitmap = android.graphics.Bitmap.createBitmap(
                        page.width,
                        page.height,
                        android.graphics.Bitmap.Config.ARGB_8888
                    )
                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                    this.setImageBitmap(bitmap)
                }

                // 关闭 PdfRenderer 和文件描述符
                pdfRenderer.close()
                parcelFileDescriptor.close()
            }
        }
    )
}
