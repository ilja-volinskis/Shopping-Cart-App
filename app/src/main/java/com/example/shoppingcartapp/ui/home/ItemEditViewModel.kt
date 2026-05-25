package com.example.shoppingcartapp.ui.home

import androidx.lifecycle.ViewModel
import com.example.shoppingcartapp.data.ItemsRepository
import com.example.shoppingcartapp.ui.cart.ItemDetailsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ItemEditViewModel(
    private val itemsRepository: ItemsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ItemDetailsUiState())
    val uiState: StateFlow<ItemDetailsUiState> = _uiState.asStateFlow()

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}