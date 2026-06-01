package com.example.shoppingcartapp.ui.cart

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
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
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val cartUiState = viewModel.uiState.collectAsStateWithLifecycle()

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
        CartItemList(
            items = cartUiState.value.itemList,
            modifier = Modifier,
            contentPadding = innerPadding
        )
    }
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
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }
    }
}


@Composable
fun CartItemCard(
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

            }
        }
    }
}
