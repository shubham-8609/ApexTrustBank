package com.codeleg.apextrustbank

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.codeleg.apextrustbank.TransactionAdapter.ViewHolder

class TransactionAdapter(private val context: Context, private  val traansactions : List<Transaction>) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.transaction_item , parent, false )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        holder.tvTypeView.text = traansactions[position].type
        holder.tvAmountView.text = traansactions[position].amount.toString()
        holder.tvDateView.text = traansactions[position].timeStamp.toString()
        holder.tvTransactionIdView.text = traansactions[position].id.toString()


    }

    override fun getItemCount(): Int {
        return traansactions.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
         val tvTypeView = itemView.findViewById<TextView>(R.id.tvType)
         val tvAmountView = itemView.findViewById<TextView>(R.id.tvAmount)
         val tvDateView = itemView.findViewById<TextView>(R.id.tvDate)
         val tvTransactionIdView = itemView.findViewById<TextView>(R.id.tvTransactionId)


    }
}