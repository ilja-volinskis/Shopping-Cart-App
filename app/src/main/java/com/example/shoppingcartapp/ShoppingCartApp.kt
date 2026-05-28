package com.example.shoppingcartapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.shoppingcartapp.ui.navigation.CartNavHost

@Composable
fun ShoppingCartApp(navController: NavHostController = rememberNavController()) {
    CartNavHost(navController = navController)
}