package com.example.shoppingcartapp.ui.cart

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun ItemDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: ItemDetailsViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    Text(
        text = "Item Details Screen"
    )
}