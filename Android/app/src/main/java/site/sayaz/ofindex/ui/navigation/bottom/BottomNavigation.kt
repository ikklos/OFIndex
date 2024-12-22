package site.sayaz.ofindex.ui.navigation.bottom

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import site.sayaz.ofindex.ui.navigation.Route

@Composable
fun BottomNavigation(
    onClickAction: (String) -> Unit
) {
    val items = BottomItems.getList()
    val selectedItem = remember { mutableIntStateOf(0) }
    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    if (selectedItem.intValue == index) {
                        Icon(painterResource(item.selectedIcon),
                            contentDescription = item.description)
                    } else {
                        Icon(painterResource(item.unselectedIcon),
                            contentDescription = item.description)
                    }
                },
                label = { Text(item.title) },
                selected = selectedItem.intValue == index,
                onClick = {
                    selectedItem.intValue = index
                    onClickAction(when(index){
                        0 -> Route.explore()
                        1 -> Route.shelf()
                        2 -> Route.forum()
                        3 -> Route.more()
                        else -> Route.explore()
                    }
                )}
            )
        }
    }
}