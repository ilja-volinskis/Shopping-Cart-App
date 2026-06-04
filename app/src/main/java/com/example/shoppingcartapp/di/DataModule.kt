package com.example.shoppingcartapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.shoppingcartapp.data.CartRepository
import com.example.shoppingcartapp.data.ItemDao
import com.example.shoppingcartapp.data.ItemsRepository
import com.example.shoppingcartapp.data.LocalDbItemRepository
import com.example.shoppingcartapp.data.PREFERENCE_NAME
import com.example.shoppingcartapp.data.ShoppingCartDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ShoppingCartDatabase =
        ShoppingCartDatabase.getInstance(context)

    @Provides
    fun provideItemDao(database: ShoppingCartDatabase): ItemDao =
        database.itemDao()

    @Provides
    @Singleton
    fun provideItemsRepository(dao: ItemDao): ItemsRepository =
        LocalDbItemRepository(dao)


    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile(PREFERENCE_NAME) }
        )
    }

    @Provides
    @Singleton
    fun provideCartRepository(dataStore: DataStore<Preferences>): CartRepository {
        return CartRepository(dataStore)
    }
}