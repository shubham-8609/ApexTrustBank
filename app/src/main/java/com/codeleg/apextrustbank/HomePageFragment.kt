package com.codeleg.apextrustbank

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.lifecycle.lifecycleScope
import com.codeleg.apextrustbank.databinding.HomePageItemsBinding
import kotlinx.coroutines.launch

class HomePageFragment : Fragment(), UserValueUpdateListener {
    private var _binding: HomePageItemsBinding? = null
    private val binding get() = _binding!!
    private var listener: UserValueUpdateListener? = null
    private lateinit var tvLastTransaction: TextView
    private lateinit var tvScLastTransaction: TextView
    private lateinit var db: DBHelper
    private lateinit var transactionDao: TransactionDao

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = DBHelper.getDB(requireContext())
        transactionDao = db.transactionDao()
        if (context is UserValueUpdateListener) {
            listener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomePageItemsBinding.inflate(inflater, container, false)
        tvLastTransaction = binding.tvLastTransaction
        tvScLastTransaction = binding.tvScLastTransaction
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
            lifecycleScope.launch {
                val lastTransaction = transactionDao.getTransactionsByUser(it.getInt(ARG_USER_ID))
                if (lastTransaction.size > 2) {
                    binding.tvLastTransaction.text =
                        "${lastTransaction[0].type}  ₹${lastTransaction[0].amount}"
                    binding.tvScLastTransaction.text =
                        "${lastTransaction[1].type} ₹${lastTransaction[1].amount}"
                }else{
                    binding.tvLastTransaction.visibility = View.GONE
                    binding.tvScLastTransaction.visibility = View.GONE
                }

            }
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
        private const val ARG_USER_ID = "user_id"

        @JvmStatic
        fun newInstance(balance: String, accNo: String, username: String , userId: Int) =
            HomePageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_BALANCE, balance)
                    putString(ARG_ACC_NO, accNo)
                    putString(ARG_USERNAME, username)
                    putInt(ARG_USER_ID , userId)
                }
            }
    }
}