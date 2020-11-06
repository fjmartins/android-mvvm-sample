package com.fjmartins.forexrates.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pairs")
data class Pair(
    @PrimaryKey
    @ColumnInfo(name = "name")
    val name: String = "",
    @ColumnInfo(name = "description")
    val description: String = "",
    @ColumnInfo(name = "value")
    val value: Double = 0.0,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long = 0
)