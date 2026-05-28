package com.example.shoppingcartapp.ui.cart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shoppingcartapp.R
import com.example.shoppingcartapp.data.Item
import com.example.shoppingcartapp.ui.navigation.NavDestination

object CartDestination : NavDestination {
    override val route = "cart"
    override val titleRes = R.string.cart_screen_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navigateToItemDetails: (Int) -> Unit,
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CartViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val cartUiState = viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CartScreenTopBar(
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        CartItemList(
            items = cartUiState.value.itemList,
            modifier = Modifier,
            contentPadding = innerPadding
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreenTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.cart)
            )
        },
        scrollBehavior = scrollBehavior,
        modifier = modifier
    )
}

@Composable
fun CartItemList(
    items: List<Item>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        contentPadding = contentPadding,
        modifier = modifier
    ) {
        items(items = items, key = { it.id }) { item ->
            CartItemCard(
                item = item,
                modifier = Modifier
            )
        }
    }
}

@Composable
fun CartItemCard(
    item: Item,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(120.dp)
            .fillMaxWidth()
    ) {
        Column {
            Text(
                text = item.name
            )
            Row {
                Text(
                    text = "${item.price}$"
                )
                Text(
                    text = "${item.quantity}"
                )
            }
        }
    }
}