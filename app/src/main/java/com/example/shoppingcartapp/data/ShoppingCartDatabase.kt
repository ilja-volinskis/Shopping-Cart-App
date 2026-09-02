package com.example.shoppingcartapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(version = 2, entities = [Item::class], exportSchema = false)
abstract class ShoppingCartDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var Instance: ShoppingCartDatabase? = null
        fun getInstance(context: Context): ShoppingCartDatabase {
            return Instance ?: synchronized(this) {
                Room
                    .databaseBuilder(
                        context,
                        ShoppingCartDatabase::class.java,
                        "shopping_cart_database"
                    )
                    .fallbackToDestructiveMigration(false)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}