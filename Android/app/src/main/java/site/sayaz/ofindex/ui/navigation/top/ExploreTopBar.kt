package site.sayaz.ofindex.ui.navigation.top

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import site.sayaz.ofindex.R
import site.sayaz.ofindex.viewmodel.ExploreViewModel

@Composable
fun ExploreTopBar(
    exploreViewModel: ExploreViewModel
){
    BaseTopBar(
        title = "Explore",
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(painterResource(R.drawable.baseline_search_24), contentDescription = "Search")
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(painterResource(R.drawable.baseline_filter_list_24), contentDescription = "Filter")
            }
        }
    )
}


