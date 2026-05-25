package com.example.shoppingcartapp.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoppingcartapp.data.Item

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel()
) {
    val homeUiState = viewModel.homeUiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        topBar = {

        }
    ) { innerPadding ->
        HomeItemList(
            items = homeUiState.value.itemList,
            modifier = Modifier,
            contentPadding = innerPadding
        )
    }
}

@Composable
fun HomeItemList(
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
            HomeItemCard(
                item = item,
                modifier = Modifier
            )
        }
    }
}

@Composable
fun HomeItemCard(
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