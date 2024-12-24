package site.sayaz.ofindex.ui.screen.shelf

import androidx.annotation.RequiresPermission
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import site.sayaz.ofindex.ui.components.PDFView
import site.sayaz.ofindex.viewmodel.ShelfViewModel

@Composable
fun ShelfScreen(
    viewModel: ShelfViewModel,
    onNavigateRead: (Long) -> Unit
) {

    Box(

        modifier = Modifier.fillMaxSize().clickable(onClick = {
            onNavigateRead(1)
        })

    )
}
