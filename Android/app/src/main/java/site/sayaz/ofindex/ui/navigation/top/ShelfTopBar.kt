package site.sayaz.ofindex.ui.navigation.top

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import site.sayaz.ofindex.R
import site.sayaz.ofindex.ui.components.BaseTopBar
import site.sayaz.ofindex.viewmodel.ShelfViewModel

@Composable
fun ShelfTopBar(
    shelfViewModel: ShelfViewModel
){
    BaseTopBar(
        title = "Shelf",
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(painterResource(R.drawable.baseline_search_24), contentDescription = "Search")
            }
        }
    )
}