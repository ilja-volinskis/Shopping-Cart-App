package com.example.shoppingcartapp.test.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.shoppingcartapp.data.Item
import com.example.shoppingcartapp.ui.cart.CartBody
import com.example.shoppingcartapp.ui.theme.ShoppingCartAppTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class CartScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val item1 = Item(id = 1, name = "Widget", description = "A widget", price = 5.00, quantity = 2)
    private val item2 = Item(id = 2, name = "Gadget", description = "A gadget", price = 3.50, quantity = 1)

    private fun setContent(
        items: List<Item> = listOf(item1, item2),
        totalPrice: Double = items.sumOf { it.price * it.quantity },
        navigateToItemDetails: (Int) -> Unit = {},
        setItemCount: (Int, Int) -> Unit = { _, _ -> },
        shareCartContents: () -> Unit = {},
    ) {
        composeTestRule.setContent {
            ShoppingCartAppTheme {
                CartBody(
                    navigateToItemDetails = navigateToItemDetails,
                    setItemCount = setItemCount,
                    shareCartContents = shareCartContents,
                    totalPrice = totalPrice,
                    items = items
                )
            }
        }
    }

    @Test
    fun cartBody_Displays_Item_Names() {
        setContent()

        composeTestRule.onNodeWithText("Widget").assertIsDisplayed()
        composeTestRule.onNodeWithText("Gadget").assertIsDisplayed()
    }

    @Test
    fun cartBody_Displays_Item_Prices() {
        setContent()

        composeTestRule.onNodeWithText("$5.0").assertIsDisplayed()
        composeTestRule.onNodeWithText("$3.5").assertIsDisplayed()
    }

    @Test
    fun cartBody_Displays_Item_Descriptions() {
        setContent()

        composeTestRule.onNodeWithText("A widget").assertIsDisplayed()
        composeTestRule.onNodeWithText("A gadget").assertIsDisplayed()
    }

    @Test
    fun cartBody_Displays_Quantity_In_Cart() {
        setContent(items = listOf(item1))

        composeTestRule.onNodeWithText("2 in cart").assertIsDisplayed()
    }

    @Test
    fun cartItemCard_Click_Calls_NavigateToItemDetails_With_Correct_Id() {
        var navigatedId = -1
        setContent(navigateToItemDetails = { navigatedId = it })

        composeTestRule.onNodeWithText("Widget").performClick()

        assertEquals(item1.id, navigatedId)
    }

    @Test
    fun quantityControlRow_Decrease_Button_Calls_SetQuantity_With_Decremented_Value() {
        var updatedId = -1
        var updatedCount = -1
        setContent(
            items = listOf(item1),
            setItemCount = { id, count -> updatedId = id; updatedCount = count }
        )

        composeTestRule.onNodeWithContentDescription("Decrease").performClick()

        assertEquals(item1.id, updatedId)
        assertEquals(item1.quantity - 1, updatedCount)
    }

    @Test
    fun cartBody_Shows_Zero_Total_For_Empty_Cart() {
        setContent(items = emptyList(), totalPrice = 0.0)

        composeTestRule.onNodeWithText("Total: $0.00").assertIsDisplayed()
    }

    @Test
    fun cartBody_Renders_With_Empty_Item_List() {
        setContent(items = emptyList(), totalPrice = 0.0)

        composeTestRule.onNodeWithText("Widget").assertDoesNotExist()
    }
}
