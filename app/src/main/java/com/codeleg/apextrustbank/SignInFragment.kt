package com.codeleg.apextrustbank

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.codeleg.apextrustbank.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private lateinit var navigationListener: AuthenticationListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigationListener = requireActivity() as AuthenticationListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false)

        binding.loginButton.setOnClickListener { checkLogin() }
        binding.registerPageLink.apply {
            paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
            setOnClickListener { navigationListener.navigateToRegister() }
        }

        return binding.root
    }

    private fun checkLogin() {
        startActivity(Intent(requireActivity(), MainActivity::class.java))
        requireActivity().finishAffinity()
    }
}
