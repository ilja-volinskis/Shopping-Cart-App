package com.example.shoppingcartapp.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shoppingcartapp.CartAppTopBar
import com.example.shoppingcartapp.R
import com.example.shoppingcartapp.data.Item

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainItemEditScreen(
    navigateBack: () -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ItemEditViewModel = hiltViewModel()
) {
    BackHandler {
        navigateBack()
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier,
        topBar = {
            CartAppTopBar(
                title = uiState.value.itemDetails.name,
                navigateUp = navigateUp,
                scrollBehavior = scrollBehavior,
                canNavigateBack = true
            )
        }
    ) { innerPadding ->
        MainItemEditBody(
            isEditingEntry = uiState.value.isEditingEntry,
            isEntryValid = uiState.value.isEntryValid,
            itemDetails = uiState.value.itemDetails,
            onItemDetailsChange = { viewModel.updateItemDetails(it) },
            insertItem = viewModel::insertItem,
            updateItem = viewModel::updateItem,
            deleteItem = {
                viewModel.deleteItem(it)
                navigateUp()
            },
            modifier = Modifier,
            contentPadding = innerPadding
        )
    }
}

@Composable
fun MainItemEditBody(
    isEditingEntry: Boolean,
    isEntryValid: Boolean,
    itemDetails: ItemDetails,
    onItemDetailsChange: (ItemDetails) -> Unit,
    insertItem: (Item) -> Unit,
    updateItem: (Item) -> Unit,
    deleteItem: (Item) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        modifier = modifier
            .padding(contentPadding)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedCard(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 16.dp)
                .fillMaxWidth(),
            colors = CardDefaults.outlinedCardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            border = BorderStroke(
                2.dp,
                MaterialTheme.colorScheme.primary
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {

                // Name
                OutlinedTextField(
                    value = itemDetails.name,
                    onValueChange = { onItemDetailsChange(itemDetails.copy(name = it)) },
                    label = { Text(stringResource(R.string.name)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                // Description
                OutlinedTextField(
                    value = itemDetails.description,
                    onValueChange = { onItemDetailsChange(itemDetails.copy(description = it)) },
                    label = { Text(stringResource(R.string.description)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 100.dp)
                        .padding(bottom = 16.dp),
                    minLines = 4
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    // Price
                    OutlinedTextField(
                        value = itemDetails.price,
                        onValueChange = { onItemDetailsChange(itemDetails.copy(price = it)) },
                        label = { Text(stringResource(R.string.price)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // In cart count
                    OutlinedTextField(
                        value = itemDetails.quantity,
                        onValueChange = { onItemDetailsChange(itemDetails.copy(quantity = it)) },
                        label = { Text(stringResource(R.string.in_stock)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                Column {
                    AppButton(
                        enabled = isEntryValid,
                        text = stringResource(
                            if(isEditingEntry) R.string.save else R.string.insert
                        ),
                        onClick = {
                            if(isEditingEntry)
                                updateItem(itemDetails.toItem())
                            else
                                insertItem(itemDetails.toItem())
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    AppButton(
                        enabled = isEntryValid && isEditingEntry,
                        text = stringResource(R.string.delete),
                        onClick = { deleteItem(itemDetails.toItem()) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
