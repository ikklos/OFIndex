package site.sayaz.ofindex.ui.screen.explore

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import site.sayaz.ofindex.viewmodel.ExploreViewModel

@Composable
fun ExploreScreen(
    exploreViewModel: ExploreViewModel,
    onNavigateRead: (String) -> Unit
) {
    Button(onClick = {
        exploreViewModel.getBooks()
    }) {
        Text("asdads")
    }

}