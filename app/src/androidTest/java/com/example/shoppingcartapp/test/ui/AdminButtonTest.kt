package com.example.shoppingcartapp.test.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import com.example.shoppingcartapp.ui.admin.AdminToggleButton
import com.example.shoppingcartapp.ui.theme.ShoppingCartAppTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class AdminButtonTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun adminToggleButton_Is_Displayed() {
        composeTestRule.setContent {
            ShoppingCartAppTheme {
                AdminToggleButton(isAdmin = false, onClick = {})
            }
        }

        composeTestRule.onNodeWithContentDescription("Toggle Admin").assertIsDisplayed()
    }

    @Test
    fun adminToggleButton_Click_Invokes_OnClick() {
        var clicked = false
        composeTestRule.setContent {
            ShoppingCartAppTheme {
                AdminToggleButton(isAdmin = false, onClick = { clicked = true })
            }
        }

        composeTestRule.onNodeWithContentDescription("Toggle Admin").performClick()

        assertTrue(clicked)
    }
}
