package com.example.shoppingcartapp.test.ui

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import com.example.shoppingcartapp.data.Item
import com.example.shoppingcartapp.ui.home.ItemDetails
import com.example.shoppingcartapp.ui.home.MainItemEditBody
import com.example.shoppingcartapp.ui.home.toItem
import com.example.shoppingcartapp.ui.theme.ShoppingCartAppTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class MainItemEditScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val validDetails = ItemDetails(
        id = 1,
        name = "Widget",
        description = "A widget",
        price = "5.99",
        quantity = "10"
    )

    private fun setContent(
        isEditingEntry: Boolean = false,
        isEntryValid: Boolean = false,
        itemDetails: ItemDetails = ItemDetails(),
        onItemDetailsChange: (ItemDetails) -> Unit = {},
        insertItem: (Item) -> Unit = {},
        updateItem: (Item) -> Unit = {},
        deleteItem: (Item) -> Unit = {},
    ) {
        composeTestRule.setContent {
            ShoppingCartAppTheme {
                MainItemEditBody(
                    isEditingEntry = isEditingEntry,
                    isEntryValid = isEntryValid,
                    itemDetails = itemDetails,
                    onItemDetailsChange = onItemDetailsChange,
                    insertItem = insertItem,
                    updateItem = updateItem,
                    deleteItem = deleteItem
                )
            }
        }
    }

    @Test
    fun mainItemEditBody_Insert_Button_Disabled_When_Entry_Invalid() {
        setContent(isEditingEntry = false, isEntryValid = false)

        composeTestRule.onNodeWithText("Insert").assertIsNotEnabled()
    }

    @Test
    fun mainItemEditBody_Insert_Button_Enabled_When_Entry_Valid() {
        setContent(isEditingEntry = false, isEntryValid = true, itemDetails = validDetails)

        composeTestRule.onNodeWithText("Insert").assertIsEnabled()
    }

    @Test
    fun mainItemEditBody_Save_Button_Enabled_When_Editing_And_Valid() {
        setContent(isEditingEntry = true, isEntryValid = true, itemDetails = validDetails)

        composeTestRule.onNodeWithText("Save").assertIsEnabled()
    }

    @Test
    fun mainItemEditBody_Delete_Button_Disabled_When_Not_Editing() {
        setContent(isEditingEntry = false, isEntryValid = true, itemDetails = validDetails)

        composeTestRule.onNodeWithText("Delete").assertIsNotEnabled()
    }

    @Test
    fun mainItemEditBody_Name_Field_Change_Calls_OnItemDetailsChange() {
        var updated: ItemDetails? = null
        setContent(
            itemDetails = validDetails,
            isEntryValid = true,
            onItemDetailsChange = { updated = it }
        )

        composeTestRule.onNodeWithText("Widget").performTextReplacement("NewName")

        assertEquals("NewName", updated?.name)
    }

    @Test
    fun mainItemEditBody_Insert_Button_Click_Calls_InsertItem() {
        var insertedItem: Item? = null
        setContent(
            isEditingEntry = false,
            isEntryValid = true,
            itemDetails = validDetails,
            insertItem = { insertedItem = it }
        )

        composeTestRule.onNodeWithText("Insert").performClick()

        assertEquals(validDetails.toItem(), insertedItem)
    }
}
