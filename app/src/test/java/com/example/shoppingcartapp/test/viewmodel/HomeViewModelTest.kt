package com.example.shoppingcartapp.test.viewmodel

import app.cash.turbine.Event
import app.cash.turbine.test
import com.example.shoppingcartapp.data.CartRepository
import com.example.shoppingcartapp.data.Item
import com.example.shoppingcartapp.data.ItemsRepository
import com.example.shoppingcartapp.ui.home.HomeUiState
import com.example.shoppingcartapp.ui.home.HomeViewModel
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.collections.emptyList


@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private lateinit var itemsRepository: ItemsRepository
    private lateinit var cartRepository: CartRepository
    private lateinit var viewModel: HomeViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    private val item1 = Item(id = 1, name = "Apple", description = "desc", price = 1.0, quantity = 5)
    private val item2 = Item(id = 2, name = "Banana", description = "desc", price = 0.5, quantity = 3)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        itemsRepository = mockk(relaxed = true)
        cartRepository = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() {
        viewModel = HomeViewModel(itemsRepository, cartRepository)
    }

    @Test
    fun uiState_Initial_Value_Has_Empty_Item_List() = runTest {
        every { itemsRepository.getAllItemsStream() } returns flowOf(emptyList())
        createViewModel()

        assertEquals(emptyList<Item>(), viewModel.uiState.value.itemList)
    }

    @Test
    fun uiState_Emits_Items_From_Repository() = runTest {
        every { itemsRepository.getAllItemsStream() } returns flowOf(listOf(item1, item2))
        createViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(listOf(item1, item2), state.itemList)
        }
    }

    @Test
    fun uiState_Updates_When_Repository_Emits_New_List() = runTest {
        val items = listOf(item1, item2)
        every { itemsRepository.getAllItemsStream() } returns flowOf(emptyList(), items)
        createViewModel()

        viewModel.uiState.test {
            val last = cancelAndConsumeRemainingEvents()
                .filterIsInstance<Event.Item<HomeUiState>>()
                .last().value
            assertEquals(items, last.itemList)
        }
    }

    @Test
    fun addItemToCart_Delegates_To_CartRepository() = runTest {
        every { itemsRepository.getAllItemsStream() } returns flowOf(emptyList())
        createViewModel()

        viewModel.addItemToCart(item1)

        coVerify { cartRepository.addToCart(item1) }
    }

    @Test
    fun deleteItem_Delegates_To_ItemsRepository() = runTest {
        every { itemsRepository.getAllItemsStream() } returns flowOf(emptyList())
        createViewModel()

        viewModel.deleteItem(item1)

        coVerify { itemsRepository.deleteItem(item1) }
    }
}
