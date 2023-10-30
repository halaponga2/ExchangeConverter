package com.example.exchangeconverter.retrofit

import retrofit2.Response
import retrofit2.http.GET

interface CurrencyApi {
    @GET("/api/symbols?access_key=46262f570784fcbf51565f32b1ae93ee")
    suspend fun getCurrencies(): CurrencyResponse

    @GET("/api/latest?access_key=46262f570784fcbf51565f32b1ae93ee")
    suspend fun getExchange(): ExchangeResponse
}