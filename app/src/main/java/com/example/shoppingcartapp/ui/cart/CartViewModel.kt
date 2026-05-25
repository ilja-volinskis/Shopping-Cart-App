package com.example.shoppingcartapp.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingcartapp.data.Item
import com.example.shoppingcartapp.data.ItemsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


class CartViewModel(
    itemsRepository: ItemsRepository
) : ViewModel() {
    val uiState: StateFlow<CartUiState> = itemsRepository.getAllItemsStream().map { CartUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = CartUiState()
        )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class CartUiState(val itemList: List<Item> = listOf())
