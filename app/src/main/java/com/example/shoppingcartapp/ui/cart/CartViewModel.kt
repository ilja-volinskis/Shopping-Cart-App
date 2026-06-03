package com.example.shoppingcartapp.ui.cart

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingcartapp.data.CartRepository
import com.example.shoppingcartapp.data.Item
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val cartRepository: CartRepository
) : ViewModel() {

    val uiState: StateFlow<CartUiState> = cartRepository.cartItems.map { cartItems ->
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
        viewModelScope.launch {
            cartRepository.addToCart(item)
        }
    }

    fun removeFromCart(itemId: Int) {
        viewModelScope.launch {
            cartRepository.removeFromCart(itemId)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            cartRepository.clearCart()
        }
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

