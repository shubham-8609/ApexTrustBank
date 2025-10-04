package com.codeleg.apextrustbank

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.codeleg.apextrustbank.databinding.FragmentWithdrawBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class WithdrawFragment : BottomSheetDialogFragment() {
    var currentUser: User? = null
    lateinit var db: DBHelper
    lateinit var userDao: UserDao
    lateinit var transactionDao: TransactionDao
    lateinit var amountInput: TextInputEditText
    lateinit var btnConfirm: MaterialButton
    lateinit var binding: FragmentWithdrawBinding
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
        binding = FragmentWithdrawBinding.inflate(inflater , container , false)
        amountInput = binding.amountInput
        btnConfirm = binding.btnConfirm
        btnConfirm.isEnabled = false
        amountInput.addTextChangedListener {
            val amount = it.toString().toDoubleOrNull()
            btnConfirm.isEnabled = amount != null && amount > 0 && amount <= 50000
        }
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            currentUser = userDao.getUserById(PrefsManager.getUserId(requireContext()))
        }

        btnConfirm.setOnClickListener { withdrawLogic() }
        return binding.root
    }

    private fun withdrawLogic() {
        val user = currentUser!!
        val amount = amountInput.text.toString().toDoubleOrNull()
        if (amount == null || amount <= 0) {
            amountInput.error = "Invalid amount"
        } else if (amount > 50000) {
            amountInput.error = "Amount exceeds limit"
        } else if (user.balance < amount) {
            amountInput.error = "Insufficient balance"
        } else {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                val newBalance = user.balance - amount
                userDao.updateBalance(user.id , newBalance )
                val transaction = Transaction(0 , user.id , amount , Date(), "Withdraw")
                transactionDao.insertTransaction(transaction)
                launch(Dispatchers.Main) {
                    listener?.onValueChanged()
                }
            }
            dismiss()
        }

    }

}