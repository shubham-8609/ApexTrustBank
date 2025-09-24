package com.codeleg.apextrustbank

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.codeleg.apextrustbank.databinding.FragmentSignInBinding


class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        binding.loginButton.setOnClickListener {
          checkLogin()
        }
        return binding.root
    }

    private fun checkLogin(){
        startActivity(Intent(requireActivity() , MainActivity::class.java))
        requireActivity().finishAffinity()

    }

}
