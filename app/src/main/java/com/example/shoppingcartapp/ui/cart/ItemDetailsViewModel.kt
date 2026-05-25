package com.example.shoppingcartapp.ui.cart

import androidx.lifecycle.ViewModel
import com.example.shoppingcartapp.data.ItemsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ItemDetailsViewModel(
    private val itemsRepository: ItemsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ItemDetailsUiState())
    val uiState: StateFlow<ItemDetailsUiState> = _uiState.asStateFlow()

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class ItemDetailsUiState(
    val outOfStock: Boolean = true,
    val itemDetails: ItemDetails = ItemDetails()
)

data class ItemDetails(
    val id: Int = 0,
    val name: String = "",
    val price: String = "",
    val quantity: String = "",
)