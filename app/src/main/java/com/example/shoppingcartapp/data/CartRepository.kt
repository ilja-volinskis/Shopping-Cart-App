package com.example.shoppingcartapp.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

const val PREFERENCE_NAME = "cart"

class CartRepository(private val dataStore: DataStore<Preferences>) {
    companion object {
        private val CART_KEY = stringPreferencesKey("cart_items")
    }

    val cartItems: Flow<List<Item>> = dataStore.data.map { preferences ->
        val json = preferences[CART_KEY] ?: return@map emptyList()
        try {
            Json.decodeFromString<List<Item>>(json)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun addToCart(item: Item, count: Int = 1) {
        if(item.quantity < 1 || count < 1) {
            return
        }

        dataStore.edit { preferences ->
            val current = (preferences[CART_KEY] ?: "")
                .takeIf { it.isNotEmpty() }
                ?.let { Json.decodeFromString<List<Item>>(it) }
                ?.toMutableList() ?: mutableListOf()

            val existingIndex = current.indexOfFirst { it.id == item.id }
            if (existingIndex >= 0) {
                val existing = current[existingIndex]
                if(existing.quantity < item.quantity) {
                    current[existingIndex] = existing.copy(quantity = existing.quantity + count)
                }
            } else {
                current.add(item.copy(quantity = count))
            }

            preferences[CART_KEY] = Json.encodeToString(current)
        }
    }


    suspend fun updateItemCount(itemId: Int, count: Int) {
        dataStore.edit { preferences ->
            val current = (preferences[CART_KEY] ?: "")
                .takeIf { it.isNotEmpty() }
                ?.let { Json.decodeFromString<List<Item>>(it) }
                ?.toMutableList() ?: mutableListOf()

            val index = current.indexOfFirst { it.id == itemId }

            if (index >= 0) {
                if (count <= 0) {
                    // Remove item if count is zero or negative
                    current.removeAt(index)
                } else {
                    val existing = current[index]
                    current[index] = existing.copy(quantity = count)
                }
            }

            preferences[CART_KEY] = Json.encodeToString(current)
        }
    }


    suspend fun removeFromCart(itemId: Int) {
        dataStore.edit { preferences ->
            val current = (preferences[CART_KEY] ?: "")
                .takeIf { it.isNotEmpty() }
                ?.let { Json.decodeFromString<List<Item>>(it) }
                ?.toMutableList() ?: mutableListOf()

            current.removeAll { it.id == itemId }
            preferences[CART_KEY] = Json.encodeToString(current)
        }
    }

    suspend fun clearCart() {
        dataStore.edit { preferences ->
            preferences.remove(CART_KEY)
        }
    }
}