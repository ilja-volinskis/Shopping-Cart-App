package com.example.shoppingcartapp.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingcartapp.data.Item
import com.example.shoppingcartapp.data.ItemsRepository
import com.example.shoppingcartapp.ui.navigation.MainItemEditDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ItemEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository
) : ViewModel() {
    private val itemId: Int = checkNotNull(savedStateHandle[MainItemEditDestination.itemIdArg])

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

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class ItemEditUiState(
    val itemDetails: ItemDetails = ItemDetails(),
    val isEntryValid: Boolean = false
)

fun Item.toItemEditUiState(isEntryValid: Boolean = false): ItemEditUiState = ItemEditUiState(
    itemDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)