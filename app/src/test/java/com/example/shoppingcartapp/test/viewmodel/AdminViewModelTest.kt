package com.example.shoppingcartapp.test.viewmodel

import com.example.shoppingcartapp.ui.admin.AdminViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AdminViewModelTest {

    private lateinit var viewModel: AdminViewModel

    @Before
    fun setup() {
        viewModel = AdminViewModel()
    }

    @Test
    fun initial_IsAdmin_State_Is_False() = runTest {
        assertFalse(viewModel.isAdmin.value)
    }

    @Test
    fun toggleAdminMode_Sets_IsAdmin_To_True() = runTest {
        viewModel.toggleAdminMode()
        assertTrue(viewModel.isAdmin.value)
    }

    @Test
    fun toggleAdminMode_Toggles_Back_To_False() = runTest {
        viewModel.toggleAdminMode()
        viewModel.toggleAdminMode()
        assertFalse(viewModel.isAdmin.value)
    }
}
