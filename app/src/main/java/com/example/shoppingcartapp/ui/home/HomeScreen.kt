package com.example.shoppingcartapp.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
fun HomeScreen(
    navigateToItemDetails: (Int) -> Unit,
    navigateToItemEdit: (Int) -> Unit,
    navigateToCart: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val homeUiState = viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CartAppTopBar(
                title = stringResource(R.string.all_products),
                scrollBehavior = scrollBehavior,
                canNavigateBack = false
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToCart,
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = stringResource(R.string.open_cart)
                )
            }
        }
    ) { innerPadding ->
        HomeBody(
            openNewItemScreen = navigateToItemEdit,
            addItemToCart = viewModel::addItemToCart,
            items = homeUiState.value.itemList,
            modifier = Modifier,
            contentPadding = innerPadding
        )
    }
}

@Composable
fun HomeBody(
    openNewItemScreen: (Int) -> Unit,
    addItemToCart: (Item) -> Unit,
    items: List<Item>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        modifier = modifier
            .padding(contentPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HomeItemList(
            addItemToCart = addItemToCart,
            items = items,
            modifier = Modifier,
        )

        // pass -1 to item screen, so it knows you create new item
        AppButton(
            text = stringResource(R.string.add),
            onClick = { openNewItemScreen(-1) },
            modifier = Modifier
                .padding(top = 16.dp)
        )
    }
}

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .width(200.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(
            width = 2.dp,
            color = MaterialTheme.colorScheme.onPrimary
        ),
        contentPadding = PaddingValues(12.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun HomeItemList(
    addItemToCart: (Item) -> Unit,
    items: List<Item>,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        modifier = modifier
    ) {
        items(items = items, key = { it.id }) { item ->
            HomeItemCard(
                addItemToCart = addItemToCart,
                item = item,
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
fun HomeItemCard(
    addItemToCart: (Item) -> Unit,
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
                .fillMaxHeight(),
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

                OutlinedButton(
                    onClick = { addItemToCart(item) },
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier
                        .size(40.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.add),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
