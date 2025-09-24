package com.codeleg.apextrustbank

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.codeleg.apextrustbank.databinding.FragmentChooseSignInBinding
import java.lang.ClassCastException

class ChooseSignInFragment : Fragment() {



    private var listener: AuthenticationListener? = null
    private lateinit var binding: FragmentChooseSignInBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AuthenticationListener) {
            listener = context
        } else {
            throw ClassCastException("$context must implement OnNavigationListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChooseSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            listener?.navigateToSignIn()
        }

        binding.registerButton.setOnClickListener {
            listener?.navigateToRegister()
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null // Avoid memory leaks
    }
}
