package com.example.shoppingcartapp.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingcartapp.data.CartRepository
import com.example.shoppingcartapp.data.Item
import com.example.shoppingcartapp.data.ItemsRepository
import com.example.shoppingcartapp.ui.navigation.MainItemDetailsDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.NumberFormat
import javax.inject.Inject

@HiltViewModel
class MainItemDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository,
    private val cartRepository: CartRepository
) : ViewModel() {
    private val itemId: Int = checkNotNull(savedStateHandle[MainItemDetailsDestination.itemIdArg])

    val uiState: StateFlow<ItemUiState> = itemsRepository
        .getItemStream(itemId)
        .filterNotNull()
        .map {
            ItemUiState(itemDetails = it.toItemDetails(), outOfStock = it.quantity <= 0)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ItemUiState()
        )

    fun addItemToCart(item: Item, count: Int = 1) {
        viewModelScope.launch {
            cartRepository.addToCart(item, count)
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class ItemUiState(
    val outOfStock: Boolean = true,
    val itemDetails: ItemDetails = ItemDetails()
)

data class ItemDetails(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val price: String = "",
    val quantity: String = "",
)

fun ItemDetails.toItem(): Item = Item(
    id = id,
    name = name,
    description = description,
    price = price.toDoubleOrNull() ?: 0.0,
    quantity = quantity.toIntOrNull() ?: 0
)

fun Item.formatedPrice(): String {
    return NumberFormat.getCurrencyInstance().format(price)
}

fun Item.toItemDetails(): ItemDetails = ItemDetails(
    id = id,
    name = name,
    description = description,
    price = price.toString(),
    quantity = quantity.toString()
)
