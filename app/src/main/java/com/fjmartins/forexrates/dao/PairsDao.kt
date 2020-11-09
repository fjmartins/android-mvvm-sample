package com.fjmartins.forexrates.dao

import androidx.room.*
import com.fjmartins.forexrates.model.Pair
import io.reactivex.Single

@Dao
interface PairsDao {
    @Query("SELECT * FROM pairs")
    fun getAllPairs(): Single<List<Pair>>

    @Insert
    fun insertPair(pair: Pair): Single<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPairs(pairs: List<Pair>): Single<List<Long>>

    @Query("SELECT * FROM pairs WHERE name=:pair")
    fun getPair(pair: String): Single<Pair>

    @Update
    fun updatePair(pair: Pair): Single<Int>

    @Delete
    fun deletePair(pair: Pair): Single<Int>

    @Query("DELETE FROM pairs")
    fun deleteAll(): Single<Int>
}