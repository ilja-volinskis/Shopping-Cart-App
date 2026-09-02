package com.example.shoppingcartapp.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingcartapp.data.Item
import com.example.shoppingcartapp.data.ItemsRepository
import com.example.shoppingcartapp.ui.navigation.MainItemDetailsDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ItemEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository
) : ViewModel() {
    private val initialItemId: Int = checkNotNull(savedStateHandle[MainItemDetailsDestination.itemIdArg])
    private val currentItemId = MutableStateFlow(initialItemId)

    val uiState: MutableStateFlow<ItemEditUiState> = MutableStateFlow(ItemEditUiState())

    // if itemId < 0 we know we are creating a new item
    init {
        viewModelScope.launch {
            currentItemId.flatMapLatest { itemId ->
                if(itemId < 0) {
                    flowOf(ItemEditUiState())
                } else {
                    itemsRepository
                        .getItemStream(itemId)
                        .filterNotNull()
                        .map {
                            ItemEditUiState(
                                itemDetails = it.toItemDetails(),
                                isEntryValid = validateInput(it.toItemDetails()),
                                isEditingEntry = true
                            )
                        }
                }
            }.collect {
                uiState.value = it
            }
        }
    }

    fun updateItemDetails(itemDetails: ItemDetails) {
        uiState.value = uiState.value.copy(
            itemDetails = itemDetails,
            isEntryValid = validateInput(itemDetails)
        )
    }

    fun insertItem(item: Item) {
        viewModelScope.launch {
            val newItemId = itemsRepository.insertItem(item)
            currentItemId.value = newItemId.toInt()
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
            currentItemId.value = -1
        }
    }

    private fun validateInput(itemUiState: ItemDetails = uiState.value.itemDetails): Boolean {
        return with(itemUiState) {
            name.isNotBlank() && price.isNotBlank() && quantity.isNotBlank()
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