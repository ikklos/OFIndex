package site.sayaz.ofindex.ui.navigation.top

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import site.sayaz.ofindex.R
import site.sayaz.ofindex.ui.components.SearchBar
import site.sayaz.ofindex.viewmodel.ExploreViewModel

@Composable
fun ExploreTopBar(
    exploreViewModel: ExploreViewModel
) {
    val searchActive by exploreViewModel.searchActive.collectAsState()
    val searchQuery by exploreViewModel.searchQuery.collectAsState()
    val bookClass by exploreViewModel.currentClass.collectAsState()

    BaseTopBar(
        title = "Explore",
        actions = {
            if (searchActive) {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { exploreViewModel.updateSearchQuery(it) },
                    onSearch = {
                        exploreViewModel.search(searchQuery, bookClass, true)
                    },
                    active = searchActive,
                    onActiveChange = { exploreViewModel.toggleSearchMode(it) },
                    modifier = Modifier,
                    placeHolder = "Search"
                )
            } else {
                IconButton(onClick = {
                    exploreViewModel.toggleSearchMode(true)
                }) {
                    Icon(
                        painterResource(R.drawable.baseline_search_24),
                        contentDescription = "Search"
                    )
                }

                IconButton(onClick = {
                    exploreViewModel.toggleBottomSheet(true)
                }) {
                    Icon(
                        painterResource(R.drawable.baseline_filter_list_24),
                        contentDescription = "Filter"
                    )
                }
            }
        }
    )
}


