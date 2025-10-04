package com.codeleg.apextrustbank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.codeleg.apextrustbank.databinding.FragmentDepositBinding
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.core.widget.addTextChangedListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.util.Date

class DepositFragment : BottomSheetDialogFragment() {
    var currentUser: User? = null
    lateinit var db: DBHelper
    lateinit var userDao: UserDao
    lateinit var transactionDao: TransactionDao
    lateinit var binding: FragmentDepositBinding
    lateinit var tvAmountInput: TextInputEditText
    lateinit var btnConfirm: MaterialButton
    private var listener: UserValueUpdateListener? = null

    fun setListener(listener: UserValueUpdateListener) {
        this.listener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = DBHelper.getDB(requireContext())
        userDao = db.userDao()
        transactionDao = db.transactionDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDepositBinding.inflate(inflater, container, false)

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            currentUser = userDao.getUserById(PrefsManager.getUserId(requireContext()))
        }

        tvAmountInput = binding.amountInput
        btnConfirm = binding.btnConfirm
        btnConfirm.isEnabled = false // Disable initially

        tvAmountInput.addTextChangedListener {
            val amount = it.toString().toDoubleOrNull()
            btnConfirm.isEnabled = amount != null && amount > 0 && amount <= 50000
        }

        btnConfirm.setOnClickListener { depositLogic() }

        return binding.root
    }

    private fun depositLogic() {
        val user = currentUser!!
        val amount = tvAmountInput.text.toString().toDoubleOrNull()
        if (amount == null || amount <= 0) {
            tvAmountInput.error = "Invalid amount"
        } else if (amount > 50000) {
            tvAmountInput.error = "Amount exceeds limit"
        } else {

            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                userDao.updateBalance(user.id, user.balance + amount!!)
                transactionDao.insertTransaction(
                    Transaction(
                        0, user.id, amount!!, Date(), "Deposit"
                    )
                )
                launch(Dispatchers.Main) {
                    listener?.onValueChanged()
                }
            }
            dismiss()
        }
        }
    }

