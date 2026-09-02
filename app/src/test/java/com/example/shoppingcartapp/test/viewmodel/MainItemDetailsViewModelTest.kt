package com.example.shoppingcartapp.test.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.example.shoppingcartapp.data.CartRepository
import com.example.shoppingcartapp.data.Item
import com.example.shoppingcartapp.data.ItemsRepository
import com.example.shoppingcartapp.ui.home.ItemDetails
import com.example.shoppingcartapp.ui.home.MainItemDetailsViewModel
import com.example.shoppingcartapp.ui.navigation.MainItemDetailsDestination
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainItemDetailsViewModelTest {

    private lateinit var itemsRepository: ItemsRepository
    private lateinit var cartRepository: CartRepository

    private val testDispatcher = UnconfinedTestDispatcher()
    private val itemId = 7

    private val inStockItem = Item(id = itemId, name = "Widget", description = "Nice widget", price = 4.99, quantity = 8)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        itemsRepository = mockk()
        cartRepository = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(item: Item = inStockItem): MainItemDetailsViewModel {
        every { itemsRepository.getItemStream(itemId) } returns flowOf(item)
        return MainItemDetailsViewModel(
            savedStateHandle = SavedStateHandle(mapOf(MainItemDetailsDestination.itemIdArg to itemId)),
            itemsRepository = itemsRepository,
            cartRepository = cartRepository
        )
    }

    @Test
    fun uiState_Default_Before_Any_Is_OutOfStock_With_Empty_Details() = runTest {
        every { itemsRepository.getItemStream(itemId) } returns flowOf()
        val viewModel = MainItemDetailsViewModel(
            savedStateHandle = SavedStateHandle(mapOf(MainItemDetailsDestination.itemIdArg to itemId)),
            itemsRepository = itemsRepository,
            cartRepository = cartRepository
        )

        assertTrue(viewModel.uiState.value.outOfStock)
        assertEquals(ItemDetails(), viewModel.uiState.value.itemDetails)
    }

    @Test
    fun addItemToCart_Calls_Repository_With_Item_And_Default_Count() = runTest {
        val viewModel = createViewModel()

        viewModel.addItemToCart(inStockItem)

        coVerify { cartRepository.addToCart(inStockItem, 1) }
    }

    @Test
    fun addItemToCart_Calls_Repository_With_Custom_Count() = runTest {
        val viewModel = createViewModel()

        viewModel.addItemToCart(inStockItem, count = 3)

        coVerify { cartRepository.addToCart(inStockItem, 3) }
    }
}
