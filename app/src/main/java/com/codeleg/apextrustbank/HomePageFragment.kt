package com.codeleg.apextrustbank

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.codeleg.apextrustbank.databinding.HomePageItemsBinding

class HomePageFragment : Fragment(), UserValueUpdateListener {
    private var _binding: HomePageItemsBinding? = null
    private val binding get() = _binding!!
    private var listener: UserValueUpdateListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is UserValueUpdateListener) {
            listener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomePageItemsBinding.inflate(inflater, container, false)

        binding.cardDeposit.setOnClickListener {
            val depositFragment = DepositFragment()
            depositFragment.setListener(this)
            depositFragment.show(parentFragmentManager, "depositFragment")
        }
        binding.cardWithdraw.setOnClickListener {
            val withdrawFragment = WithdrawFragment()
            withdrawFragment.setListener(this)
            withdrawFragment.show(parentFragmentManager, "withdrawFragment")
        }
        binding.cardTransfer.setOnClickListener {
            val transferFragment = TransferFragment()
            transferFragment.setListener(this)
            transferFragment.show(parentFragmentManager, "transferFragment")
        }
        binding.cardRecent.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, TransactionFragment())
                .addToBackStack(null)
                .commit()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            binding.tvBalance.text = it.getString(ARG_BALANCE)
            binding.tvAccountNo.text = it.getString(ARG_ACC_NO)
            binding.headingCardUserTitle.text = "Welcome, ${it.getString(ARG_USERNAME)} 👋"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onValueChanged() {
        listener?.onValueChanged()
    }

    companion object {
        private const val ARG_BALANCE = "balance"
        private const val ARG_ACC_NO = "acc_no"
        private const val ARG_USERNAME = "username"

        @JvmStatic
        fun newInstance(balance: String, accNo: String, username: String) =
            HomePageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_BALANCE, balance)
                    putString(ARG_ACC_NO, accNo)
                    putString(ARG_USERNAME, username)
                }
            }
    }
}