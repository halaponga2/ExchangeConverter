package com.example.exchangeconverter.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface Dao {
    @Insert
    fun insertCurrency(currency: Currency)

    @Update(entity = Currency::class)
    fun updateExchangeRates(currencyUpdate: CurrencyUpdate)

    @Query("SELECT * FROM currencies")
    fun getCurrencies(): Flow<List<Currency>>

    @Query("SELECT * FROM currencies ORDER BY id LIMIT 1")
    fun loadlastCurrency(): Currency?

    @Query("SELECT * FROM currencies WHERE id=:id")
    fun getCurrency(id:String?): Currency?

}