package com.example.shoppingcartapp.ui.cart

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ItemDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: ItemDetailsViewModel = viewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    Text(
        text = "Item Details Screen"
    )
}