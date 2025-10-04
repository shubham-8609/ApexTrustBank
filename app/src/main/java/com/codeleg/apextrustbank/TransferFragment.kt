package com.codeleg.apextrustbank

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.codeleg.apextrustbank.databinding.FragmentTransferBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

class TransferFragment : BottomSheetDialogFragment() {

    private lateinit var userDao: UserDao
    private lateinit var transactionDao: TransactionDao
    private lateinit var db: DBHelper
    private lateinit var binding: FragmentTransferBinding
    private lateinit var accountNoInput: TextInputEditText
    private lateinit var amountInput: TextInputEditText
    private lateinit var btnConfirm: MaterialButton
    private var listener: UserValueUpdateListener? = null

    fun setListener(listener: UserValueUpdateListener) {
        this.listener = listener
    }

    private var currentUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = DBHelper.getDB(requireContext())
        userDao = db.userDao()
        transactionDao = db.transactionDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransferBinding.inflate(inflater, container, false)

        accountNoInput = binding.accountNoInput
        amountInput = binding.amountInput
        btnConfirm = binding.btnConfirm
        btnConfirm.isEnabled = false

        // Disable inputs until current user is loaded
        accountNoInput.isEnabled = false
        amountInput.isEnabled = false

        // Load current user
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            currentUser = userDao.getUserById(PrefsManager.getUserId(requireContext()))
            withContext(Dispatchers.Main) {
                accountNoInput.isEnabled = true
                amountInput.isEnabled = true
            }
        }

        // Enable confirm button only when valid input
        accountNoInput.addTextChangedListener { validateInputs() }
        amountInput.addTextChangedListener { validateInputs() }

        btnConfirm.setOnClickListener { transferLogic() }

        return binding.root
    }

    private fun validateInputs() {
        val accountNo = accountNoInput.text.toString().toIntOrNull()
        val amount = amountInput.text.toString().toDoubleOrNull()

        btnConfirm.isEnabled = accountNo != null && amount != null && amount > 0 && amount <= 50000
    }

    private fun transferLogic() {
        val accountNo = accountNoInput.text.toString().toIntOrNull()
        val amount = amountInput.text.toString().toDoubleOrNull()


        val sender = currentUser ?: run {
            DialogHelper.showSnacksbar(binding.root, "User not loaded yet")
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val recipient = userDao.getUserByAccountNo(accountNo!!.toLong())

            if (recipient == null) {
                withContext(Dispatchers.Main) {
                    accountNoInput.error = "No user found with this account number"
                }
                return@launch
            }

            // Prevent sending to own account
            if (recipient.id == sender.id) {
                withContext(Dispatchers.Main) {
                    accountNoInput.error = "Cannot transfer to your own account"
                }
                return@launch
            }

            // Check sufficient balance
            if (sender.balance < amount!!) {
                withContext(Dispatchers.Main) {
                    amountInput.error = "Insufficient balance"
                }
                return@launch
            }

            // --- Perform transfer ---
            val newSenderBalance = sender.balance - amount
            val newRecipientBalance = recipient.balance + amount

            userDao.updateBalance(sender.id, newSenderBalance)
            userDao.updateBalance(recipient.id, newRecipientBalance)

            val time = Date()
            transactionDao.insertTransaction(Transaction(0, sender.id, amount, time, "Transfer"))
            transactionDao.insertTransaction(Transaction(0, recipient.id, amount, time, "Received"))

            withContext(Dispatchers.Main) {
                DialogHelper.showSnacksbar(binding.root, "₹$amount transferred successfully!")
                listener?.onValueChanged()
                dismiss() // close the bottom sheet
            }
        }
    }
}