package com.codeleg.apextrustbank

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.codeleg.apextrustbank.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var navigationListener: AuthenticationListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigationListener = requireActivity() as AuthenticationListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val registerButton = binding.registerButton
        binding.signInPageLink.apply {
            paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
            setOnClickListener { navigationListener.navigateToSignIn() }
        }
        registerButton.setOnClickListener {  startActivity(Intent(requireActivity(), MainActivity::class.java))
            requireActivity().finishAffinity() }

        return binding.root
    }
}
