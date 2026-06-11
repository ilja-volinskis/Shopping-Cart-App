package com.example.shoppingcartapp.test.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.shoppingcartapp.ui.cart.CartItemDetailsBody
import com.example.shoppingcartapp.ui.home.ItemDetails
import com.example.shoppingcartapp.ui.theme.ShoppingCartAppTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class CartItemDetailsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val inStockItem = ItemDetails(
        id = 1,
        name = "Widget",
        description = "A widget",
        price = "9.99",
        quantity = "3"
    )

    private fun setContent(
        itemDetails: ItemDetails = inStockItem,
        actualItemQuantity: Int = 10,
        removeFromCart: (Int) -> Unit = {},
        setItemCountInCart: (Int) -> Unit = {},
    ) {
        composeTestRule.setContent {
            ShoppingCartAppTheme {
                CartItemDetailsBody(
                    actualItemQuantity = actualItemQuantity,
                    itemDetails = itemDetails,
                    removeFromCart = removeFromCart,
                    setItemCountInCart = setItemCountInCart
                )
            }
        }
    }

    @Test
    fun cartItemDetailsBody_Displays_Price() {
        setContent()

        composeTestRule.onNodeWithText("$9.99").assertIsDisplayed()
    }

    @Test
    fun cartItemDetailsBody_Shows_Out_Of_Stock_Label_When_None_Available() {
        setContent(actualItemQuantity = 0)

        composeTestRule.onNodeWithText("Out of stock").assertIsDisplayed()
    }

    @Test
    fun cartItemDetailsBody_Displays_Remove_From_Cart_Button() {
        setContent()

        composeTestRule.onNodeWithText("Remove from cart").assertIsDisplayed()
    }

    @Test
    fun cartItemDetailsBody_Remove_Button_Calls_RemoveFromCart_And_Navigates() {
        var removedId = -1
        var navigated = false
        setContent(removeFromCart = {
            removedId = it
            navigated = true
        })

        composeTestRule.onNodeWithText("Remove from cart").performClick()

        assertEquals(inStockItem.id, removedId)
        assertTrue(navigated)
    }

    @Test
    fun cartItemDetailsBody_Increase_Button_Does_Not_Exceed_Stock() {
        var newCount = -1
        setContent(
            itemDetails = inStockItem.copy(quantity = "5"),
            actualItemQuantity = 5,
            setItemCountInCart = { newCount = it }
        )

        composeTestRule.onNodeWithContentDescription("Increase").performClick()

        assertEquals(5, newCount)
    }
}
