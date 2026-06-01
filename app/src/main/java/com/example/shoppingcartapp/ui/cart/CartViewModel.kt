package com.example.shoppingcartapp.ui.cart

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingcartapp.data.Item
import com.example.shoppingcartapp.data.ItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository
) : ViewModel() {

    private val _cartItems = savedStateHandle.getStateFlow(CART_KEY, emptyList<Item>())

    val uiState: StateFlow<CartUiState> = _cartItems.map { cartItems ->
        CartUiState(
            cartItems = cartItems,
            totalPrice = cartItems.sumOf { it.price * it.quantity }
        )
    }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = CartUiState()
        )

    fun addToCart(item: Item) {
    }

    fun removeFromCart(itemId: Int) {
    }

    fun updateQuantity(itemId: Int, quantity: Int) {
    }

    fun clearCart() {
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
        private const val CART_KEY = "cart_items"
    }
}

data class CartUiState(
    val cartItems: List<Item> = listOf(),
    val totalPrice: Double = 0.0
)

