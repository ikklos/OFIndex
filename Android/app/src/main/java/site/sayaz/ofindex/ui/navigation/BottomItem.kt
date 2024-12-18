package site.sayaz.ofindex.ui.navigation

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
        R.drawable.ic_android_black_24dp,
        R.drawable.ic_android_black_24dp,
        "Explore")
    private val shelf = BottomItem(
        "Shelf",
        R.drawable.ic_android_black_24dp,
        R.drawable.ic_android_black_24dp,
        "Shelf")
    private val forum = BottomItem(
        "Forum",
        R.drawable.ic_android_black_24dp,
        R.drawable.ic_android_black_24dp,
        "Forum")
    private val more = BottomItem(
        "More",
        R.drawable.ic_android_black_24dp,
        R.drawable.ic_android_black_24dp,
        "More")

    fun getList(): List<BottomItem> {
        return listOf(explore, shelf, forum, more)
    }
}
