package site.sayaz.ofindex.ui.screen.explore

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import site.sayaz.ofindex.ui.components.BookListView
import site.sayaz.ofindex.viewmodel.ExploreViewModel


@Composable
fun ExploreScreen(
    exploreViewModel: ExploreViewModel,
    onNavigateBookDetail: (Long) -> Unit
) {
    val searchResults by exploreViewModel.searchResults.collectAsState()
    val searchActive by exploreViewModel.searchActive.collectAsState()
    val searchQuery by exploreViewModel.searchQuery.collectAsState()
    val currentClass by exploreViewModel.currentClass.collectAsState()
    val showBottomSheet by exploreViewModel.showBottomSheet.collectAsState()
    val classList by exploreViewModel.classList.collectAsState()



    LaunchedEffect(Unit) {
        exploreViewModel.search(searchQuery, currentClass, true)
        exploreViewModel.getClassList()
    }

    BookListView(
        books = searchResults,
        onBookClick = onNavigateBookDetail,
        onLoadMore = {exploreViewModel.search(searchQuery, currentClass, false)}
    )

    if (showBottomSheet){
        ExploreBottomSheet(
            onDismiss = { exploreViewModel.toggleBottomSheet(false) },
            classList = classList,
            onClassSelected = { exploreViewModel.updateBookClass(it) },
            initialSelectedClassId = 0,
            selectedClassId = currentClass
        )
    }
}