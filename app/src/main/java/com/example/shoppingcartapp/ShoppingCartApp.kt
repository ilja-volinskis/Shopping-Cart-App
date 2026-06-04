package com.example.shoppingcartapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.shoppingcartapp.ui.admin.AdminViewModel
import com.example.shoppingcartapp.ui.navigation.CartNavHost
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ShoppingCartApp(
    navController: NavHostController = rememberNavController(),
    adminViewModel: AdminViewModel = hiltViewModel()
) {
    val isAdmin = adminViewModel.isAdmin.collectAsStateWithLifecycle()

    CartNavHost(
        navController = navController,
        isAdmin = isAdmin.value,
        toggleAdmin = adminViewModel::toggleAdminMode
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartAppTopBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        }
    )
}
