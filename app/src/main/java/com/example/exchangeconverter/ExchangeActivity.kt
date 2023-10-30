package com.example.exchangeconverter

import android.R.attr.button
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.exchangeconverter.databinding.ActivityExchangeBinding
import com.example.exchangeconverter.room.MainDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ExchangeActivity : AppCompatActivity() {
    lateinit var binding: ActivityExchangeBinding
    lateinit var db: MainDB
    var fromCurrencyId: String? = ""
    var toCurrencyId: String? = ""
    var fromRate: Float? = 0.0F
    var toRate: Float? = 0.0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExchangeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("selected:","${intent.getStringExtra("selectedCurrency")}")

        if (intent.getStringExtra("selectedCurrency") == "TextView"){
            fromCurrencyId = intent.getStringExtra("clickedCurrency")
            toCurrencyId = "EUR"
            Log.d("Selected:", "False")

        }
        else{
            fromCurrencyId = intent.getStringExtra("selectedCurrency")
            toCurrencyId = intent.getStringExtra("clickedCurrency")

            Log.d("Selected:", "True")
        }



        db = MainDB.getDB(this)

        getRates()

        binding.fromValueView.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().trim { it <= ' ' }.length == 0) {
                    binding.convertButton.setEnabled(false)
                } else {
                    binding.convertButton.setEnabled(true)
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
            }
        })





    }

    private fun getRates(){
        lifecycleScope.launch(Dispatchers.IO){
            fromRate= db.getDao().getCurrency(fromCurrencyId)?.exchangeRate
            toRate = db.getDao().getCurrency(toCurrencyId)?.exchangeRate
            binding.fromCurrencyView.text = db.getDao().getCurrency(fromCurrencyId)?.fullName
            binding.toCurrencyView.text = db.getDao().getCurrency(toCurrencyId)?.fullName
        }
    }

    fun onClickConvert(view: View){
        val value: Float = binding.fromValueView.text.toString().toFloat()
        val result: Float = value/ fromRate!! * toRate!!
        binding.toValueView.text = result.toString()
    }
}