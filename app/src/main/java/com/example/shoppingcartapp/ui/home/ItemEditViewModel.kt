package com.example.shoppingcartapp.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingcartapp.data.CartRepository
import com.example.shoppingcartapp.data.Item
import com.example.shoppingcartapp.data.ItemsRepository
import com.example.shoppingcartapp.ui.navigation.MainItemDetailsDestination
import com.example.shoppingcartapp.ui.navigation.MainItemEditDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository
) : ViewModel() {
    private val itemId: Int = checkNotNull(savedStateHandle[MainItemDetailsDestination.itemIdArg])

    // if itemId < 0 we know we are creating a new item
    val uiState: StateFlow<ItemEditUiState> =
        (
            if(itemId < 0)
                flowOf(ItemEditUiState())
             else
                itemsRepository
                    .getItemStream(itemId)
                    .filterNotNull()
                    .map {
                        ItemEditUiState(
                            itemDetails = it.toItemDetails(),
                            isEntryValid = false,
                            isEditingEntry = true
                        )
                    }
        ).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ItemEditUiState()
        )

    fun insertItem(item: Item) {
        viewModelScope.launch {
            itemsRepository.insertItem(item)
        }
    }

    fun updateItem(item: Item) {
        viewModelScope.launch {
            itemsRepository.updateItem(item)
        }
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch {
            itemsRepository.deleteItem(item)
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class ItemEditUiState(
    val itemDetails: ItemDetails = ItemDetails(),
    val isEditingEntry: Boolean = false,
    val isEntryValid: Boolean = false
)

fun Item.toItemEditUiState(isEntryValid: Boolean = false): ItemEditUiState = ItemEditUiState(
    itemDetails = this.toItemDetails(),
    isEntryValid = isEntryValid,
    isEditingEntry = true
)