package com.example.shoppingcartapp.ui.cart

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.shoppingcartapp.R
import com.example.shoppingcartapp.ui.navigation.NavDestination

object CartItemDetailsDestination : NavDestination {
    override val route = "cart_item_details"
    override val titleRes = R.string.cart_item_details_screen_title
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@Composable
fun CartItemDetailsScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CartItemDetailsViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    Text(
        text = "Item Details Screen"
    )
}