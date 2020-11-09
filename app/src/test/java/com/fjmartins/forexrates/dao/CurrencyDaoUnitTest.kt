package com.fjmartins.forexrates.dao

import androidx.room.Room
import com.fjmartins.forexrates.base.BaseUnitTest
import com.fjmartins.forexrates.model.Currency
import org.junit.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CurrencyDaoUnitTest : BaseUnitTest() {

    private lateinit var forexDb: ForexDatabase

    @Before
    fun initialize() {
        forexDb = Room.databaseBuilder(
            context,
            ForexDatabase::class.java, "forex-database-test"
        ).allowMainThreadQueries() // Only for testing
            .build()

        forexDb.clearAllTables()
    }

    @Test
    fun insertTest() {
        val testCurrency =
            Currency(name = "TEST_CURRENCY", description = "TEST_DESCRIPTION", timestamp = 100L)

        val result = forexDb.currencyDao().insertCurrency(testCurrency).blockingGet()
        assertEquals(1, result)
    }

    @Test
    fun insertMultipleTest() {
        val testCurrencies = ArrayList<Currency>().apply{
            add(Currency(name = "TEST_CURRENCY", description = "TEST_DESCRIPTION", timestamp = 100L))
            add(Currency(name = "TEST_CURRENCY_1", description = "TEST_DESCRIPTION_1", timestamp = 100L))
        }

        val result = forexDb.currencyDao().insertCurrencies(testCurrencies).blockingGet()
        assertEquals(testCurrencies.size, result.size)
    }

    @Test
    fun readTest() {
        val testCurrency =
            Currency(name = "TEST_CURRENCY", description = "TEST_DESCRIPTION", timestamp = 100L)

        forexDb.currencyDao().insertCurrency(testCurrency).blockingGet()
        forexDb.currencyDao().getCurrency(testCurrency.name).test().assertValue { currency ->
            currency.name == testCurrency.name && currency.description == testCurrency.description && currency.timestamp == testCurrency.timestamp
        }
    }

    @Test
    fun updateTest() {
        val testCurrency =
            Currency(name = "TEST_CURRENCY", description = "TEST_DESCRIPTION", timestamp = 100L)

        forexDb.currencyDao().insertCurrency(testCurrency).blockingGet()
        testCurrency.description = "UPDATED_DESCRIPTION"

        forexDb.currencyDao().updateCurrency(testCurrency).blockingGet()
        forexDb.currencyDao().getCurrency(testCurrency.name).test().assertValue { currency ->
            currency.name == testCurrency.name && currency.description == testCurrency.description && currency.timestamp == testCurrency.timestamp
        }
    }

    @Test
    fun deleteTest() {
        val testCurrency =
            Currency(name = "TEST_CURRENCY", description = "TEST_DESCRIPTION", timestamp = 100L)

        forexDb.currencyDao().insertCurrency(testCurrency).blockingGet()
        val deletedRows = forexDb.currencyDao().deleteCurrency(testCurrency).blockingGet()
        assertEquals(1, deletedRows)
    }

    @Test
    fun deleteMultipleTest() {
        val testCurrencies = ArrayList<Currency>().apply{
            add(Currency(name = "TEST_CURRENCY", description = "TEST_DESCRIPTION", timestamp = 100L))
            add(Currency(name = "TEST_CURRENCY_1", description = "TEST_DESCRIPTION_1", timestamp = 100L))
        }
        forexDb.currencyDao().insertCurrencies(testCurrencies).blockingGet()

        val result = forexDb.currencyDao().deleteAll().blockingGet()
        assertEquals(testCurrencies.size, result)
    }

    @After
    fun finalize() {
        forexDb.clearAllTables()

        forexDb.close()
    }
}