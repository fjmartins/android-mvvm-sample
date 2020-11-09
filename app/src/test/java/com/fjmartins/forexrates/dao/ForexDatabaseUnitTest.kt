package com.fjmartins.forexrates.dao

import androidx.room.Room
import com.fjmartins.forexrates.base.BaseUnitTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

//All tests must be independent
@RunWith(RobolectricTestRunner::class)
class ForexDatabaseUnitTest : BaseUnitTest() {

    private var currencyDao: CurrencyDao? = null
    private var pairsDao: PairsDao? = null
    private lateinit var forexDb : ForexDatabase

    @Before
    fun initializeDb() {
        forexDb = Room.databaseBuilder(
            context,
            ForexDatabase::class.java, "forex-database"
        ).build()

        currencyDao = forexDb.currencyDao()
        pairsDao = forexDb.pairsDao()
    }

    @Test
    fun databaseInitializedSuccessfully() {
        assertEquals(true, pairsDao != null)
        assertEquals(true, currencyDao != null)
    }

    @After
    fun closeDb() {
        forexDb.close()
    }
}