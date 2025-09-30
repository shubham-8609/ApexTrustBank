package com.codeleg.apextrustbank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.codeleg.apextrustbank.databinding.FragmentDepositBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DepositFragment : BottomSheetDialogFragment() {

    lateinit var binding : FragmentDepositBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDepositBinding.inflate(inflater, container, false)
        return binding.root
    }

}