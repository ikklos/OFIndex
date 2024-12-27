package site.sayaz.ofindex.ui.navigation.bottom

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import site.sayaz.ofindex.ui.navigation.Route

@Composable
fun BottomNavigation(
    onClickAction: (String) -> Unit,
    currentRoute: String?
) {
    val items = BottomItems.getList()
    val selectedItem = remember { mutableIntStateOf(0) }
    LaunchedEffect (currentRoute){
        selectedItem.intValue = when(currentRoute) {
            Route.explore() -> 0
            Route.shelf() -> 1
            Route.forum() -> 2
            Route.profile() -> 3
            else -> -1
        }
    }
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
                        3 -> Route.profile()
                        else -> Route.explore()
                    }
                )},
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )


            )
        }
    }
}