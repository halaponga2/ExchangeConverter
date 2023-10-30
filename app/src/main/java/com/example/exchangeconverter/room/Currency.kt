package com.example.exchangeconverter.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "currencies")
data class Currency(
    @PrimaryKey()
    var id: String,
    @ColumnInfo(name = "fullName")
    var fullName:String,
    @ColumnInfo(name = "exchangeRate")
    var exchangeRate:Float?,
)

@Entity (tableName = "currencies")
data class CurrencyUpdate(
    var id: String,
    var exchangeRate:Float?,
)
