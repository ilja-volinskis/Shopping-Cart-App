package com.example.shoppingcartapp.test.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.example.shoppingcartapp.data.CartRepository
import com.example.shoppingcartapp.data.Item
import com.example.shoppingcartapp.ui.cart.CartViewModel
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
class CartViewModelTest {

    private lateinit var cartRepository: CartRepository
    private lateinit var viewModel: CartViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    private val item1 = Item(id = 1, name = "Apple", description = "desc", price = 2.0, quantity = 3)
    private val item2 = Item(id = 2, name = "Banana", description = "desc", price = 1.5, quantity = 2)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        cartRepository = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() {
        viewModel = CartViewModel(
            savedStateHandle = SavedStateHandle(),
            cartRepository = cartRepository
        )
    }

    @Test
    fun uiState_Initial_Value_Is_Empty_Cart() = runTest {
        every { cartRepository.cartItems } returns flowOf(emptyList())
        createViewModel()

        assertEquals(emptyList<Item>(), viewModel.uiState.value.cartItems)
        assertEquals(0.0, viewModel.uiState.value.totalPrice, 0.001)
    }

    @Test
    fun setItemCount_Delegates_To_CartRepository() = runTest {
        every { cartRepository.cartItems } returns flowOf(emptyList())
        createViewModel()

        viewModel.setItemCount(itemId = 1, count = 5)

        coVerify { cartRepository.updateItemCount(1, 5) }
    }

    @Test
    fun addToCart_Delegates_To_CartRepository() = runTest {
        every { cartRepository.cartItems } returns flowOf(emptyList())
        createViewModel()

        viewModel.addToCart(item1)

        coVerify { cartRepository.addToCart(item1) }
    }

    @Test
    fun removeFromCart_Delegates_To_CartRepository() = runTest {
        every { cartRepository.cartItems } returns flowOf(emptyList())
        createViewModel()

        viewModel.removeFromCart(itemId = 1)

        coVerify { cartRepository.removeFromCart(1) }
    }

    @Test
    fun clearCart_Delegates_To_CartRepository() = runTest {
        every { cartRepository.cartItems } returns flowOf(emptyList())
        createViewModel()

        viewModel.clearCart()

        coVerify { cartRepository.clearCart() }
    }
}
