package com.fjmartins.forexrates.dao

import androidx.room.*
import com.fjmartins.forexrates.model.Currency
import io.reactivex.Single

@Dao
interface CurrencyDao {

    @Query("SELECT * FROM currencies")
    fun getAllCurrencies(): Single<List<Currency>>

    @Insert
    fun insertCurrency(currencies: Currency): Single<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrencies(currencies: List<Currency>): Single<List<Long>>

    @Update
    fun updateCurrency(currencies: Currency)

    @Delete
    fun deleteCurrency(currencies: Currency)
}