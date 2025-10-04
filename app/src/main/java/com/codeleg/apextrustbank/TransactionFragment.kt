package com.codeleg.apextrustbank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codeleg.apextrustbank.databinding.FragmentTransactionBinding
import java.util.Date

class TransactionFragment : Fragment() {
    lateinit var binding: FragmentTransactionBinding
    lateinit var transactionRecyclerView: RecyclerView
    val transactions = mutableListOf<Transaction>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTransactionBinding.inflate(inflater , container , false  )
        transactionRecyclerView = binding.transactionRecycler
        transactionRecyclerView.layoutManager

        // Add 20 dummy objects to the transactions list
        for (i in 1..20) {
            transactions.add(
                Transaction(
                    i,
                    i*5,
                    (100 + i * 10).toDouble(), // Example amount
                    Date(),
                    "Deposit"
                )
            )
        }
         val recyclerAdapter  = TransactionAdapter(requireActivity() ,transactions )

        transactionRecyclerView.adapter = recyclerAdapter
      return binding.root
    }


}
