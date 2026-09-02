package com.example.shoppingcartapp.data

import kotlinx.coroutines.flow.Flow

interface ItemsRepository {
    fun getAllItemsStream(): Flow<List<Item>>
    fun getItemStream(id: Int): Flow<Item?>

    suspend fun insertItem(item: Item): Long
    suspend fun deleteItem(item: Item)
    suspend fun updateItem(item: Item)
}
