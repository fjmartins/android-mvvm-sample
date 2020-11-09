package com.fjmartins.forexrates.dao

import androidx.room.Room
import com.fjmartins.forexrates.base.BaseUnitTest
import com.fjmartins.forexrates.model.Pair
import org.junit.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PairsDaoUnitTest : BaseUnitTest() {

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
        val testPair =
            Pair(name = "TEST_PAIR", value = 1.0, timestamp = 100L)

        val result = forexDb.pairsDao().insertPair(testPair).blockingGet()
        assertEquals(1, result)
    }

    @Test
    fun insertMultipleTest() {
        val testPairs = ArrayList<Pair>().apply{
            add(Pair(name = "TEST_PAIR", value = 1.0, timestamp = 100L))
            add(Pair(name = "TEST_PAIR_1", value = 2.0, timestamp = 100L))
        }

        val result = forexDb.pairsDao().insertPairs(testPairs).blockingGet()
        assertEquals(testPairs.size, result.size)
    }

    @Test
    fun readTest() {
        val testPair =
            Pair(name = "TEST_PAIR", value = 1.0, timestamp = 100L)

        forexDb.pairsDao().insertPair(testPair).blockingGet()
        forexDb.pairsDao().getPair(testPair.name).test().assertValue { pair ->
            pair.name == testPair.name && pair.value == testPair.value && pair.timestamp == testPair.timestamp
        }
    }

    @Test
    fun updateTest() {
        val testPair =
            Pair(name = "TEST_PAIR", value = 1.0, timestamp = 100L)

        forexDb.pairsDao().insertPair(testPair).blockingGet()
        testPair.value = 5.0

        forexDb.pairsDao().updatePair(testPair).blockingGet()
        forexDb.pairsDao().getPair(testPair.name).test().assertValue { pair ->
            pair.name == testPair.name && pair.value == testPair.value && pair.timestamp == testPair.timestamp
        }
    }

    @Test
    fun deleteTest() {
        val testPair =
            Pair(name = "TEST_PAIR", value = 1.0, timestamp = 100L)

        forexDb.pairsDao().insertPair(testPair).blockingGet()
        val deletedRows = forexDb.pairsDao().deletePair(testPair).blockingGet()
        assertEquals(1, deletedRows)
    }

    @Test
    fun deleteMultipleTest() {
        val testPairs = ArrayList<Pair>().apply{
            add(Pair(name = "TEST_PAIR", value = 1.0, timestamp = 100L))
            add(Pair(name = "TEST_PAIR_1", value = 2.0, timestamp = 100L))
        }

        forexDb.pairsDao().insertPairs(testPairs).blockingGet()

        val result = forexDb.pairsDao().deleteAll().blockingGet()
        assertEquals(testPairs.size, result)
    }

    @After
    fun finalize() {
        forexDb.clearAllTables()

        forexDb.close()
    }
}