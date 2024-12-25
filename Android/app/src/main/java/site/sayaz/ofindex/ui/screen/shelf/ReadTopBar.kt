package site.sayaz.ofindex.ui.screen.shelf

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import site.sayaz.ofindex.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadTopBar(
    onNavigateBack: () -> Unit,
    onToggleBottomBar: (Boolean) -> Unit
){
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = { onNavigateBack() }) {
                Icon(
                    painterResource(R.drawable.baseline_arrow_back_24),
                    contentDescription = "back"
                )
            }
        },
        actions = {
            IconButton(onClick = {
                onToggleBottomBar(true)
            }) {
                Icon(
                    painterResource(R.drawable.package_2_24px),
                    contentDescription = "open pack"
                )
            }
        }
    )
}
