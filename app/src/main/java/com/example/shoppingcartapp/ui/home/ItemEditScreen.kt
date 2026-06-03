package com.example.shoppingcartapp.ui.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.shoppingcartapp.R

@Composable
fun MainItemEditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ItemEditViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState

    Text(
        text = "Item Edit Screen"
    )
}