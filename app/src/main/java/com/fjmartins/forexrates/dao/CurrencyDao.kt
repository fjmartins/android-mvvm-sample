package com.fjmartins.forexrates.dao

import androidx.room.*
import com.fjmartins.forexrates.model.Currency
import io.reactivex.Single

@Dao
interface CurrencyDao {

    @Query("SELECT * FROM currencies")
    fun getAllCurrencies(): Single<List<Currency>>

    @Insert
    fun insertCurrency(currency: Currency): Single<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrencies(currencies: List<Currency>): Single<List<Long>>

    @Query("SELECT * FROM currencies WHERE name=:currency")
    fun getCurrency(currency: String): Single<Currency>

    @Update
    fun updateCurrency(currencies: Currency): Single<Int>

    @Delete
    fun deleteCurrency(currencies: Currency): Single<Int>

    @Query("DELETE FROM currencies")
    fun deleteAll(): Single<Int>
}