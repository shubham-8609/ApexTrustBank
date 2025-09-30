package com.codeleg.apextrustbank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.codeleg.apextrustbank.databinding.FragmentHomePageBinding
import com.codeleg.apextrustbank.databinding.HomePageItemsBinding
import com.google.android.material.card.MaterialCardView

class HomePageFragment : Fragment() {
    lateinit var binding: HomePageItemsBinding
   lateinit var depositCard: MaterialCardView
   lateinit var withdrawCard: MaterialCardView
   lateinit var transferCard: MaterialCardView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = HomePageItemsBinding.inflate(inflater , container , false)
        
        depositCard = binding.cardDeposit
        withdrawCard = binding.cardWithdraw
        transferCard = binding.cardTransfer
        depositCard.setOnClickListener {
            DepositFragment().show(parentFragmentManager , "depositFragment")
        }
        withdrawCard.setOnClickListener {
            WithdrawFragment().show(parentFragmentManager , "withdrawFragment" )
        }
        transferCard.setOnClickListener {
            TransferFragment().show(parentFragmentManager , "transferFragment")
        }
        return binding.root // Return the bound view
    }
   

  



}