package com.example.shoppingcartapp.test.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.shoppingcartapp.data.CartRepository
import com.example.shoppingcartapp.data.Item
import com.example.shoppingcartapp.data.ItemsRepository
import com.example.shoppingcartapp.ui.cart.CartItemDetailsViewModel
import com.example.shoppingcartapp.ui.home.toItemDetails
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
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CartItemDetailsViewModelTest {

    private lateinit var itemsRepository: ItemsRepository
    private lateinit var cartRepository: CartRepository

    private val testDispatcher = UnconfinedTestDispatcher()
    private val itemId = 42

    private val cartItem = Item(id = itemId, name = "Widget", description = "A widget", price = 9.99, quantity = 2)
    private val repoItem = Item(id = itemId, name = "Widget", description = "A widget", price = 9.99, quantity = 10)

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

    private fun createViewModel(): CartItemDetailsViewModel {
        val savedStateHandle = SavedStateHandle(
            mapOf(MainItemDetailsDestination.itemIdArg to itemId)
        )
        return CartItemDetailsViewModel(savedStateHandle, itemsRepository, cartRepository)
    }

    @Test
    fun uiState_Combines_Cart_Item_And_Repository_Stock_Quantity() = runTest {
        every { cartRepository.cartItems } returns flowOf(listOf(cartItem))
        every { itemsRepository.getItemStream(itemId) } returns flowOf(repoItem)

        val viewModel = createViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(cartItem.toItemDetails(), state.itemDetails)
            assertEquals(repoItem.quantity, state.actualItemQuantity)
        }
    }

    @Test
    fun setItemCountInCart_Delegates_To_CartRepository_With_Correct_Args() = runTest {
        every { cartRepository.cartItems } returns flowOf(listOf(cartItem))
        every { itemsRepository.getItemStream(itemId) } returns flowOf(repoItem)

        val viewModel = createViewModel()
        viewModel.setItemCountInCart(5)

        coVerify { cartRepository.updateItemCount(itemId, 5) }
    }

    @Test
    fun removeItemFromCart_Delegates_To_CartRepository_With_Item_Id() = runTest {
        every { cartRepository.cartItems } returns flowOf(listOf(cartItem))
        every { itemsRepository.getItemStream(itemId) } returns flowOf(repoItem)

        val viewModel = createViewModel()
        viewModel.removeItemFromCart(itemId)

        coVerify { cartRepository.removeFromCart(itemId) }
    }
}
