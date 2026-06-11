package com.example.shoppingcartapp.test.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.shoppingcartapp.data.Item
import com.example.shoppingcartapp.ui.home.HomeBody
import com.example.shoppingcartapp.ui.theme.ShoppingCartAppTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val item1 = Item(id = 1, name = "Apple", description = "Fresh apple", price = 1.99, quantity = 5)
    private val item2 = Item(id = 2, name = "Banana", description = "Yellow banana", price = 0.99, quantity = 0)

    private fun setContent(
        items: List<Item> = listOf(item1, item2),
        isAdmin: Boolean = false,
        navigateToItemDetails: (Int) -> Unit = {},
        openNewItemScreen: () -> Unit = {},
        addItemToCart: (Item) -> Unit = {},
        navigateToItemEdit: (Int) -> Unit = {},
        deleteItem: (Item) -> Unit = {},
    ) {
        composeTestRule.setContent {
            ShoppingCartAppTheme {
                HomeBody(
                    navigateToItemDetails = navigateToItemDetails,
                    openNewItemScreen = openNewItemScreen,
                    addItemToCart = addItemToCart,
                    items = items,
                    isAdmin = isAdmin,
                    navigateToItemEdit = navigateToItemEdit,
                    deleteItem = deleteItem
                )
            }
        }
    }

    @Test
    fun homeBody_Displays_Item_Names() {
        setContent()

        composeTestRule.onNodeWithText("Apple").assertIsDisplayed()
        composeTestRule.onNodeWithText("Banana").assertIsDisplayed()
    }

    @Test
    fun homeBody_Displays_Item_Prices() {
        setContent()

        composeTestRule.onNodeWithText("$1.99").assertIsDisplayed()
        composeTestRule.onNodeWithText("$0.99").assertIsDisplayed()
    }

    @Test
    fun homeBody_Displays_Item_Descriptions() {
        setContent()

        composeTestRule.onNodeWithText("Fresh apple").assertIsDisplayed()
        composeTestRule.onNodeWithText("Yellow banana").assertIsDisplayed()
    }

    @Test
    fun homeBody_Shows_Out_Of_Stock_Label_When_Quantity_Is_Zero() {
        setContent(items = listOf(item2))

        composeTestRule.onNodeWithText("No items in stock").assertIsDisplayed()
    }

    @Test
    fun homeBody_Does_Not_Show_Add_Button_When_Not_Admin() {
        setContent(isAdmin = false)

        composeTestRule.onNodeWithText("Add").assertDoesNotExist()
    }

    @Test
    fun homeBody_Shows_Add_Button_When_Admin() {
        setContent(isAdmin = true)

        composeTestRule.onNodeWithText("Add").assertIsDisplayed()
    }

    @Test
    fun HomeItemCard_Click_Calls_NavigateToItemDetails_With_Correct_Id() {
        var navigatedId = -1
        setContent(navigateToItemDetails = { navigatedId = it })

        composeTestRule.onNodeWithText("Apple").performClick()

        assertEquals(item1.id, navigatedId)
    }

    @Test
    fun homeBody_Add_New_Item_Button_Calls_OpenNewItemScreen() {
        var called = false
        setContent(isAdmin = true, openNewItemScreen = { called = true })

        composeTestRule.onNodeWithText("Add").performClick()

        assertTrue(called)
    }

    @Test
    fun homeBody_Renders_With_Empty_Item_List() {
        setContent(items = emptyList())

        composeTestRule.onNodeWithText("Apple").assertDoesNotExist()
    }
}
