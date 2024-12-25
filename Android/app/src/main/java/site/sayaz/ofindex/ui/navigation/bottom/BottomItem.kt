package site.sayaz.ofindex.ui.navigation.bottom

import site.sayaz.ofindex.R

data class BottomItem (
    val title: String = "",
    val selectedIcon: Int = 0,
    val unselectedIcon: Int = 0,
    val description: String = "",
)

object BottomItems{
    private val explore = BottomItem(
        "Explore",
        R.drawable.baseline_explore_24,
        R.drawable.outline_explore_24,
        "Explore")
    private val shelf = BottomItem(
        "Shelf",
        R.drawable.baseline_collections_bookmark_24,
        R.drawable.outline_collections_bookmark_24,
        "Shelf")
    private val forum = BottomItem(
        "Forum",
        R.drawable.baseline_forum_24,
        R.drawable.outline_forum_24,
        "Forum")
    private val more = BottomItem(
        "More",
        R.drawable.ic_android_black_24dp,
        R.drawable.ic_android_black_24dp,
        "More")

    fun getList(): List<BottomItem> {
        return listOf(explore, shelf, forum)
    }
}
