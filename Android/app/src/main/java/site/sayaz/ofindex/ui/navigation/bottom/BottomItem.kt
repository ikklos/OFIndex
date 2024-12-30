package site.sayaz.ofindex.ui.navigation.bottom

import site.sayaz.ofindex.R

data class BottomItem (
    val title: Int = 0,
    val selectedIcon: Int = 0,
    val unselectedIcon: Int = 0,
    val description: String = "",
)

object BottomItems{
    private val explore = BottomItem(
        R.string.action_explore,
        R.drawable.baseline_explore_24,
        R.drawable.outline_explore_24,
        "Explore")
    private val shelf = BottomItem(
        R.string.action_shelf,
        R.drawable.baseline_collections_bookmark_24,
        R.drawable.outline_collections_bookmark_24,
        "Shelf")
    private val forum = BottomItem(
        R.string.action_forum,
        R.drawable.baseline_forum_24,
        R.drawable.outline_forum_24,
        "Forum")
//    private val more = BottomItem(
//        R.string.action_more,
//        R.drawable.ic_android_black_24dp,
//        R.drawable.ic_android_black_24dp,
//        "More")
    private val profile = BottomItem(
        R.string.action_profile,
        R.drawable.baseline_person_24,
        R.drawable.outline_person_24,
        "Profile")

    fun getList(): List<BottomItem> {
        return listOf(explore, shelf, forum, profile)
    }
}
