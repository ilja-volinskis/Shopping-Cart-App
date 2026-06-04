package com.example.shoppingcartapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.shoppingcartapp.ui.cart.CartItemDetailsScreen
import com.example.shoppingcartapp.ui.cart.CartScreen
import com.example.shoppingcartapp.ui.home.HomeScreen
import com.example.shoppingcartapp.ui.home.MainItemDetailsScreen
import com.example.shoppingcartapp.ui.home.MainItemEditScreen

@Composable
fun CartNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToItemDetails = {
                    navController.navigate("${MainItemDetailsDestination.route}/${it}")
                },
                navigateToItemEdit = {
                    navController.navigate("${MainItemEditDestination.route}/${it}")
                },
                navigateToCart = {
                    navController.navigate(CartDestination.route)
                }
            )
        }
        composable(
            route = MainItemDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(MainItemDetailsDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            MainItemDetailsScreen(
                navigateBack = { navController.popBackStack() },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = MainItemEditDestination.routeWithArgs,
            arguments = listOf(navArgument(MainItemEditDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            MainItemEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }

        composable(route = CartDestination.route) {
            CartScreen(
                navigateToItemDetails = {
                    navController.navigate("${CartItemDetailsDestination.route}/${it}")
                },
                navigateBack = { navController.popBackStack() },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = CartItemDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(CartItemDetailsDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            CartItemDetailsScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}