package site.sayaz.ofindex.ui.components

import android.content.Context
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.widget.ImageView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import java.io.File

@Composable
fun PDFView(modifier: Modifier, pdfBytes: ByteArray, page: Int) {
    val context = LocalContext.current
    val tempFile = File(context.cacheDir, "temp.pdf")
    tempFile.writeBytes(pdfBytes)
    val parcelFileDescriptor = ParcelFileDescriptor.open(tempFile, ParcelFileDescriptor.MODE_READ_ONLY)
    val pdfRenderer = PdfRenderer(parcelFileDescriptor)
    AndroidView(
        modifier = modifier,
        factory = { viewContext: Context ->
            ImageView(viewContext).apply {
                renderPDFPage(pdfRenderer, page)
            }
        },
        update = {view ->
            view.renderPDFPage(pdfRenderer, page)
        }
    )
}

private fun ImageView.renderPDFPage(pdfRenderer: PdfRenderer, pageIndex: Int) {
    pdfRenderer.openPage(pageIndex).use { page ->
        val bitmap = android.graphics.Bitmap.createBitmap(
            page.width,
            page.height,
            android.graphics.Bitmap.Config.ARGB_8888
        )
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        this.setImageBitmap(bitmap)
    }
}
