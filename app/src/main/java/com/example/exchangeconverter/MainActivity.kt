package com.example.exchangeconverter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.exchangeconverter.adapter.CurrencyAdapter
import com.example.exchangeconverter.databinding.ActivityMainBinding
import com.example.exchangeconverter.retrofit.CurrencyApi
import com.example.exchangeconverter.room.Currency
import com.example.exchangeconverter.room.CurrencyUpdate
import com.example.exchangeconverter.room.MainDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), CurrencyAdapter.CurrencyFunctions {
    private lateinit var adapter: CurrencyAdapter
    lateinit var binding: ActivityMainBinding
    lateinit var db: MainDB
    var selectedCurrency:String? = null
    lateinit var clickedCurrency:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = CurrencyAdapter(this)
        binding.currenciesRcView.layoutManager = LinearLayoutManager(this)
        binding.currenciesRcView.adapter = adapter

        val interceptor= HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://data.fixer.io/").client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val currencyApi = retrofit.create(CurrencyApi::class.java)

        db = MainDB.getDB(this)

        CoroutineScope(Dispatchers.IO).launch {

            if (db.getDao().loadlastCurrency() == null){
                Log.d ("DB exist", "false")
                val currencyMap = currencyApi.getCurrencies()
                val exchangeRatesMap = currencyApi.getExchange()

                currencyMap.symbols.forEach{
                    entry ->
                    val currency = Currency(entry.key,entry.value,
                        exchangeRatesMap.rates[entry.key]
                    )
                    db.getDao().insertCurrency(currency)
                }


            }
            else {
                Log.d ("DB exist", "true")
                val exchangeRatesMap = currencyApi.getExchange()
                exchangeRatesMap.rates.forEach{
                    entry ->
                    val currencyUpdate = CurrencyUpdate(entry.key, entry.value)
                    db.getDao().updateExchangeRates(currencyUpdate)
                }

            }

            runOnUiThread {

                observeItems()

            }


        }
    }

    private fun observeItems() {
        lifecycleScope.launch{
            binding.apply {
                db.getDao().getCurrencies().collect() {itemList ->
                    if (itemList.isNotEmpty()) {
                        adapter.submitList(itemList)
                    }
                }

            }
        }
    }


    override fun selectCurrency(name:String,fullName:String){
        binding.apply {
            selectedLayoutView.visibility = View.VISIBLE
            selectedTitleView.text= name
            selectedDescriptionView.text=fullName
            selectedCurrency = selectedTitleView.text.toString()
        }
    }

    override fun clickCurrency(name: String) {
        val intent = Intent(this,ExchangeActivity::class.java)
        intent.putExtra("selectedCurrency",binding.selectedTitleView.text.toString())
        intent.putExtra("clickedCurrency",name)
        startActivity(intent)
    }



}