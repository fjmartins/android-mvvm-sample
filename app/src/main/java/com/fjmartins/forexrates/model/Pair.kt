package com.fjmartins.forexrates.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pairs")
data class Pair(
    @PrimaryKey
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "value")
    var value: Double = 0.0,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long = 0
)