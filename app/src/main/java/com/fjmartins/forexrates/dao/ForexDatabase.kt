package com.fjmartins.forexrates.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fjmartins.forexrates.model.Currency
import com.fjmartins.forexrates.model.Pair

@Database(entities = [Pair::class, Currency::class], version = 1)
abstract class ForexDatabase: RoomDatabase() {
    abstract fun pairsDao(): PairsDao
    abstract fun currencyDao(): CurrencyDao
}