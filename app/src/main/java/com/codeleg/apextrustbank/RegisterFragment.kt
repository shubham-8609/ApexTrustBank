package com.codeleg.apextrustbank

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.codeleg.apextrustbank.databinding.FragmentRegisterBinding
import kotlinx.coroutines.launch

class  RegisterFragment : Fragment() {
    private  var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var navigationListener: AuthenticationListener
    private lateinit var usernameEditText: TextView
    private lateinit var passwordEditText: TextView
    private lateinit var confirmPasswordEditText: TextView
    private lateinit var registerButton: TextView
    private lateinit var signInPageLink: TextView
    private lateinit var db: DBHelper
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigationListener = requireActivity() as AuthenticationListener
        db = DBHelper.getDB(requireActivity())
       userDao = db.userDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        usernameEditText = binding.usernameEditText
        passwordEditText = binding.passwordEditText
        confirmPasswordEditText = binding.confirmPasswordEditText
        registerButton = binding.registerButton
        signInPageLink = binding.signInPageLink
        signInPageLink.apply {
            paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
            setOnClickListener { navigationListener.navigateToSignIn() }
        }
        registerButton.setOnClickListener {  checkInputs() }

        return binding.root
    }

    private fun checkInputs(){
        val username = usernameEditText.text.toString().trim()
        val password = passwordEditText.text.toString()
        val confirmPassword = confirmPasswordEditText.text.toString()
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            DialogHelper.showSnacksbar(binding.root , "Please fill all the fields")
        } else if (password != confirmPassword) {
            DialogHelper.showSnacksbar(binding.root, "Passwords do not match")
        } else {
            register(username , password)
        }
    }

    private fun register(username: String, password: String) {
        // Launch coroutine (because Room operations must not run on main thread)
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // Check if username already exists
                val existingUser = userDao.getUserByUsername(username)
                if (existingUser != null) {
                    DialogHelper.showSnacksbar(binding.root, "Username already taken")
                    return@launch
                }

                // Create new user (assuming User has fields: id, username, passwordHash, balance, etc.)
                val newUser = User.create(username , password , 0.0 , 0)

                val userId = userDao.insertUser(newUser)

                if (userId > 0) {
                    DialogHelper.showSnacksbar(binding.root, "Registration successful")
                    // Navigate to main screen or login
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                    requireActivity().finishAffinity()
                } else {
                    DialogHelper.showSnacksbar(binding.root, "Registration failed")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                DialogHelper.showSnacksbar(binding.root, "Error: ${e.message}")
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding  = null
    }
}
