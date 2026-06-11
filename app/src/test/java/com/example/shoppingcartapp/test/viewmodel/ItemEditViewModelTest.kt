package com.example.shoppingcartapp.test.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.example.shoppingcartapp.data.Item
import com.example.shoppingcartapp.data.ItemsRepository
import com.example.shoppingcartapp.ui.home.ItemDetails
import com.example.shoppingcartapp.ui.home.ItemEditViewModel
import com.example.shoppingcartapp.ui.navigation.MainItemDetailsDestination
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ItemEditViewModelTest {

    private lateinit var itemsRepository: ItemsRepository

    private val testDispatcher = UnconfinedTestDispatcher()
    private val itemId = 5

    private val existingItem = Item(id = itemId, name = "Gadget", description = "A gadget", price = 12.50, quantity = 3)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        itemsRepository = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModelForEdit(item: Item = existingItem): ItemEditViewModel {
        every { itemsRepository.getItemStream(item.id) } returns flowOf(item)
        return ItemEditViewModel(
            savedStateHandle = SavedStateHandle(mapOf(MainItemDetailsDestination.itemIdArg to item.id)),
            itemsRepository = itemsRepository
        )
    }

    private fun createViewModelForCreate(): ItemEditViewModel {
        return ItemEditViewModel(
            savedStateHandle = SavedStateHandle(mapOf(MainItemDetailsDestination.itemIdArg to -1)),
            itemsRepository = itemsRepository
        )
    }

    @Test
    fun init_Create_All_Fields_Are_Empty() = runTest {
        val viewModel = createViewModelForCreate()
        val details = viewModel.uiState.value.itemDetails
        assertEquals("", details.name)
        assertEquals("", details.description)
        assertEquals("", details.price)
        assertEquals("", details.quantity)
    }

    @Test
    fun updateItemDetails_IsEntryValid_False_When_Name_Is_Blank() = runTest {
        val viewModel = createViewModelForCreate()

        viewModel.updateItemDetails(ItemDetails(name = "", price = "1.0", quantity = "2"))

        assertFalse(viewModel.uiState.value.isEntryValid)
    }

    @Test
    fun updateItemDetails_IsEntryValid_False_When_Name_Is_Only_Whitespace() = runTest {
        val viewModel = createViewModelForCreate()

        viewModel.updateItemDetails(ItemDetails(name = "   ", price = "1.0", quantity = "2"))

        assertFalse(viewModel.uiState.value.isEntryValid)
    }

    @Test
    fun insertItem_Switches_State_To_Edit_Mode() = runTest {
        val newId = 99
        coEvery { itemsRepository.insertItem(any()) } returns newId.toLong()
        every { itemsRepository.getItemStream(newId) } returns flowOf(existingItem.copy(id = newId))
        val viewModel = createViewModelForCreate()

        viewModel.insertItem(existingItem.copy(id = 0))

        assertTrue(viewModel.uiState.value.isEditingEntry)
    }

    @Test
    fun deleteItem_Calls_Repository_With_Correct_Item() = runTest {
        val viewModel = createViewModelForEdit()

        viewModel.deleteItem(existingItem)

        coVerify { itemsRepository.deleteItem(existingItem) }
    }
}
