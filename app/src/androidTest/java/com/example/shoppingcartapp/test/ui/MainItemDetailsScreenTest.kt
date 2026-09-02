package com.example.shoppingcartapp.test.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.shoppingcartapp.data.Item
import com.example.shoppingcartapp.ui.home.ItemDetails
import com.example.shoppingcartapp.ui.home.MainItemDetailsBody
import com.example.shoppingcartapp.ui.home.toItem
import com.example.shoppingcartapp.ui.theme.ShoppingCartAppTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class MainItemDetailsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val inStockDetails = ItemDetails(
        id = 1,
        name = "Widget",
        description = "A really nice widget",
        price = "9.99",
        quantity = "6"
    )

    private fun setContent(
        outOfStock: Boolean = false,
        itemDetails: ItemDetails = inStockDetails,
        addToCart: (Item, Int) -> Unit = { _, _ -> },
    ) {
        composeTestRule.setContent {
            ShoppingCartAppTheme {
                MainItemDetailsBody(
                    outOfStock = outOfStock,
                    itemDetails = itemDetails,
                    addToCart = addToCart
                )
            }
        }
    }

    @Test
    fun mainItemDetailsBody_Displays_Info() {
        setContent()

        composeTestRule.onNodeWithText("A really nice widget").assertIsDisplayed()
        composeTestRule.onNodeWithText("$9.99").assertIsDisplayed()
    }

    @Test
    fun mainItemDetailsBody_Shows_Out_Of_Stock_Label_When_None_Available() {
        setContent(outOfStock = true)

        composeTestRule.onNodeWithText("Out of stock").assertIsDisplayed()
    }

    @Test
    fun mainItemDetailsBody_Displays_Add_To_Cart_Button() {
        setContent()

        composeTestRule.onNodeWithText("Add to cart").assertIsDisplayed()
    }

    @Test
    fun mainItemDetailsBody_Displays_Quantity_Control_Row() {
        setContent()

        composeTestRule.onNodeWithContentDescription("Decrease").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Increase").assertIsDisplayed()
    }

    @Test
    fun mainItemDetailsBody_Add_To_Cart_Button_Calls_AddToCart_With_Item_And_Quantity() {
        var calledItem: Item? = null
        var calledCount = -1
        setContent(addToCart = { item, count -> calledItem = item; calledCount = count })

        composeTestRule.onNodeWithText("Add to cart").performClick()

        assertEquals(inStockDetails.toItem(), calledItem)
        assertEquals(1, calledCount)
    }
}
