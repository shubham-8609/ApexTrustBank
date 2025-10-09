package com.codeleg.apextrustbank

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
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
    private  val TRANSACTION_CHANNEL_NAME = "Transactions"
    private val TRANSACTION_CHANNEL_ID = "TRANSACTION_CHANNEL"
    lateinit var  notificationManager: NotificationManager


    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = DBHelper.getDB(requireContext())
        transactionDao = db.transactionDao()
        if (context is UserValueUpdateListener) {
            listener = context
        }
        notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()
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
        activity?.title = "Dashboard"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            lifecycleScope.launch {
            binding.tvBalance.text = it.getString(ARG_BALANCE)
            binding.tvAccountNo.text = it.getString(ARG_ACC_NO)
            binding.headingCardUserTitle.text = "Welcome, ${it.getString(ARG_USERNAME)} 👋"
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

    override fun onResume() {
        super.onResume()
        activity?.title = getString(R.string.app_name)
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

     override fun showNotification(message: String){
        val intent = PendingIntent.getActivity(requireContext() , 0 , Intent(requireContext() ,
            MainActivity::class.java) , PendingIntent.FLAG_IMMUTABLE)
        val bitmapDrawable = ContextCompat.getDrawable(requireContext() , R.drawable.bank) as BitmapDrawable
        val largeIcon = bitmapDrawable.bitmap
        val notification = NotificationCompat.Builder(requireContext(), TRANSACTION_CHANNEL_ID).apply {
            setSmallIcon(R.drawable.bank)
            setLargeIcon(largeIcon)
            setContentTitle("Transaction Alert")
            setContentText(message)
            setContentIntent(intent)
            setAutoCancel(true)


        }
            .build()
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    private fun createNotificationChannel() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(TRANSACTION_CHANNEL_ID , TRANSACTION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
        }
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