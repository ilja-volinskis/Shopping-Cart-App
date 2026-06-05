package com.example.shoppingcartapp.ui.cart

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingcartapp.data.CartRepository
import com.example.shoppingcartapp.data.ItemsRepository
import com.example.shoppingcartapp.ui.home.ItemDetails
import com.example.shoppingcartapp.ui.home.ItemUiState
import com.example.shoppingcartapp.ui.home.toItemDetails
import com.example.shoppingcartapp.ui.navigation.MainItemDetailsDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartItemDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository,
    private val cartRepository: CartRepository
) : ViewModel() {
    private val itemId: Int = checkNotNull(savedStateHandle[MainItemDetailsDestination.itemIdArg])

    // Out of stock info is taken from item repository, but item details are from cart
    val uiState: StateFlow<CartDetailsUiState> = combine(
        cartRepository.cartItems.map { cartItems ->
            cartItems.find { it.id == itemId }
        }.filterNotNull(),
        itemsRepository.getItemStream(itemId).filterNotNull()
    ) { cartItem, repositoryItem ->
        CartDetailsUiState(
            itemDetails = cartItem.toItemDetails(),
            actualItemQuantity = repositoryItem.quantity
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = CartDetailsUiState()
    )

    fun setItemCountInCart(count: Int) {
        viewModelScope.launch {
            cartRepository.updateItemCount(itemId, count)
        }
    }

    fun removeItemFromCart(itemId: Int) {
        viewModelScope.launch {
            cartRepository.removeFromCart(itemId)
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class CartDetailsUiState(
    val actualItemQuantity: Int = 0,
    val itemDetails: ItemDetails = ItemDetails()
)