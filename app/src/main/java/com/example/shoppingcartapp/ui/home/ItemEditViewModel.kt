package com.example.shoppingcartapp.ui.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository
) : ViewModel() {

    var uiState by mutableStateOf(ItemEditUiState())
        private set

    private val itemId: Int = checkNotNull(savedStateHandle[MainItemEditDestination.itemIdArg])

    // itemId < 0 if you want to create new item from this screen
    init {
        viewModelScope.launch {
            uiState = if(itemId < 0) {
                ItemEditUiState()
            } else {
                itemsRepository
                    .getItemStream(itemId)
                    .filterNotNull()
                    .first()
                    .toItemEditUiState(true)
            }
        }
    }

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