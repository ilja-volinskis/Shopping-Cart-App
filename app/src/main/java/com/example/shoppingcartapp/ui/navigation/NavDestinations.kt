package com.example.shoppingcartapp.ui.navigation

import com.example.shoppingcartapp.R

interface NavDestination {
    val route: String
    val titleRes: Int
}

object HomeDestination : NavDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

object MainItemEditDestination : NavDestination {
    override val route = "main_item_edit"
    override val titleRes = R.string.main_item_edit_screen_title
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

object MainItemDetailsDestination : NavDestination {
    override val route = "main_item_details"
    override val titleRes = R.string.main_item_details_screen_title
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}


object CartDestination : NavDestination {
    override val route = "cart"
    override val titleRes = R.string.cart_screen_title
}

object CartItemDetailsDestination : NavDestination {
    override val route = "cart_item_details"
    override val titleRes = R.string.cart_item_details_screen_title
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}
