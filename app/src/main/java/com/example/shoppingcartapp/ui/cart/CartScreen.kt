package com.example.shoppingcartapp.ui.cart

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shoppingcartapp.CartAppTopBar
import com.example.shoppingcartapp.R
import com.example.shoppingcartapp.data.Item

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navigateToItemDetails: (Int) -> Unit,
    navigateBack: () -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CartViewModel = hiltViewModel()
) {
    BackHandler {
        navigateBack()
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val cartUiState = viewModel.uiState.collectAsStateWithLifecycle()

    Log.d("Cart", "${cartUiState.value.cartItems}")

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CartAppTopBar(
                title = stringResource(R.string.cart),
                navigateUp = navigateUp,
                scrollBehavior = scrollBehavior,
                canNavigateBack = true
            )
        }
    ) { innerPadding ->
        CartBody(
            navigateToItemDetails = navigateToItemDetails,
            totalPrice = cartUiState.value.totalPrice,
            items = cartUiState.value.cartItems,
            modifier = Modifier,
            contentPadding = innerPadding
        )
    }
}

@Composable
fun CartBody(
    navigateToItemDetails: (Int) -> Unit,
    totalPrice: Double,
    items: List<Item>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Box(
        modifier = modifier
            .padding(contentPadding)
    ) {
        CartItemList(
            navigateToItemDetails = navigateToItemDetails,
            items = items,
            modifier = Modifier
                .fillMaxSize()
        )

        TotalPriceDisplay(
            totalPrice = totalPrice,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 20.dp, bottom = 32.dp, end = 20.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun TotalPriceDisplay(
    totalPrice: Double,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(200.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.medium
            )
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onPrimary,
                shape = MaterialTheme.shapes.medium
            )
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = String.format("Total: $%.2f", totalPrice),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CartItemList(
    navigateToItemDetails: (Int) -> Unit,
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
                navigateToItemDetails = navigateToItemDetails,
                item = item,
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }
    }
}


@Composable
fun CartItemCard(
    navigateToItemDetails: (Int) -> Unit,
    item: Item,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier
            .height(120.dp)
            .fillMaxWidth(),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface)
    ) {
        Row (
            modifier = Modifier
                .fillMaxHeight()
                .clickable { navigateToItemDetails(item.id) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.ic_broken_image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.weight(1f)
            )
            Row(
                modifier = Modifier
                    .weight(2.5f)
                    .padding(start = 4.dp, top = 4.dp, bottom = 4.dp, end = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "$${item.price}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = item.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    val itemsLeftText = if (item.quantity == 0) stringResource(R.string.no_items_in_stock)
                    else "${item.quantity} left"
                    Text(
                        text = itemsLeftText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(Modifier.weight(1f))

            }
        }
    }
}
