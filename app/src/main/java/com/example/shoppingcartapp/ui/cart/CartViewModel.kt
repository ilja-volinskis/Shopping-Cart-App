package com.example.shoppingcartapp.ui.cart

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
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
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


    fun setItemCount(itemId: Int, count: Int) {
        viewModelScope.launch {
            cartRepository.updateItemCount(itemId, count)
        }
    }

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

    fun buildCartContentsJson(): String {
        val payload = SharePayload(
            items = uiState.value.cartItems
                .map { ItemNoId(it.name, it.description, it.price, it.quantity) },
            totalPrice = uiState.value.totalPrice
        )
        val json = Json.encodeToString(payload)

        return json
    }

    @Serializable
    private data class ItemNoId(
        val name: String,
        val description: String,
        val price: Double,
        val quantity: Int
    )
    @Serializable
    private data class SharePayload(
        val items: List<ItemNoId>,
        val totalPrice: Double
    )



    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
        private const val CART_KEY = "cart_items"
    }
}

data class CartUiState(
    val cartItems: List<Item> = listOf(),
    val totalPrice: Double = 0.0
)

