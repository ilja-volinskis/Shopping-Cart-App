package com.example.shoppingcartapp.ui.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ItemEditScreen(
    modifier: Modifier = Modifier,
    viewModel: ItemEditViewModel = viewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    Text(
        text = "Item Edit Screen"
    )
}