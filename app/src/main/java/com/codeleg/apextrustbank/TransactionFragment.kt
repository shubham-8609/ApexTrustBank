package com.codeleg.apextrustbank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.room.TransactionScope
import com.codeleg.apextrustbank.databinding.FragmentTransactionBinding
import kotlinx.coroutines.launch
import java.util.Date

class TransactionFragment : Fragment() {
    lateinit var binding: FragmentTransactionBinding
    lateinit var transactionRecyclerView: RecyclerView
    val transactions = mutableListOf<Transaction>()
     var currentUser:User? = null
    lateinit var db: DBHelper
    lateinit var userDao: UserDao
    lateinit var transactionDao: TransactionDao
     var userTransactions:List<Transaction>? = null
    lateinit var tvNoTransactions: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        db = DBHelper.getDB(requireContext())
        userDao = db.userDao()
        transactionDao = db.transactionDao()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTransactionBinding.inflate(inflater , container , false  )
        transactionRecyclerView = binding.transactionRecycler
        transactionRecyclerView.layoutManager
        tvNoTransactions = binding.tvNoTransactions

        lifecycleScope.launch {
            currentUser = userDao.getUserById(PrefsManager.getUserId(requireContext()))
            userTransactions = transactionDao.getTransactionsByUser(currentUser!!.id)

            val recyclerAdapter = if (userTransactions.isNullOrEmpty()){
                showNoTransactions()
                null
            }else{
                TransactionAdapter(requireActivity(), userTransactions!!)
            }
            transactionRecyclerView.adapter = recyclerAdapter
        }
      return binding.root
    }

    private fun showNoTransactions(){
        transactionRecyclerView.visibility = View.GONE
        tvNoTransactions.visibility = View.VISIBLE
    }


}
