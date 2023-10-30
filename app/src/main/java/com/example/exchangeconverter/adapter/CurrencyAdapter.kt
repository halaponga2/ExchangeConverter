package com.example.exchangeconverter.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.exchangeconverter.R
import com.example.exchangeconverter.databinding.ListItemBinding
import com.example.exchangeconverter.room.Currency

class CurrencyAdapter(private val currencyFunctions: CurrencyFunctions): ListAdapter<Currency,CurrencyAdapter.Holder>(Comparator()) {

    class Holder(view: View) : RecyclerView.ViewHolder(view){
        val binding = ListItemBinding.bind(view)

        fun bind(currency: Currency) = with(binding){
            cardTitleView.text = currency.id
            cardDescriptionView.text = currency.fullName
        }
    }

    class Comparator : DiffUtil.ItemCallback<Currency>(){
        override fun areItemsTheSame(oldItem: Currency, newItem: Currency): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Currency, newItem: Currency): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item,parent,false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
        holder.binding.cardEntity.setOnClickListener{
            Log.d("Click:","click on ${getItem(position).id}.")
            currencyFunctions.clickCurrency(holder.binding.cardTitleView.text.toString())
        }

        holder.binding.cardEntity.setOnLongClickListener {
            currencyFunctions.selectCurrency(holder.binding.cardTitleView.text.toString(),holder.binding.cardDescriptionView.text.toString())
            true
        }

    }

    interface CurrencyFunctions{
        fun selectCurrency(name:String,fullName:String)
        fun clickCurrency(name: String)
    }



}