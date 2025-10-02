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
    import com.codeleg.apextrustbank.databinding.FragmentSignInBinding
    import kotlinx.coroutines.launch

    class SignInFragment : Fragment() {
        private lateinit var _binding: FragmentSignInBinding
        private val binding get() = _binding!!
        private lateinit var usernameEditText: TextView
        private lateinit var passwordEditText: TextView
        private lateinit var loginButton: TextView
        private lateinit var registerPageLink: TextView
        private lateinit var db: DBHelper
        private lateinit var userDao: UserDao

        private lateinit var navigationListener: AuthenticationListener

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            navigationListener = requireActivity() as AuthenticationListener
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _binding = FragmentSignInBinding.inflate(inflater, container, false)
            usernameEditText = binding.usernameEditText
            passwordEditText = binding.passwordEditText
            loginButton = binding.loginButton
            registerPageLink = binding.registerPageLink
            loginButton.setOnClickListener { checkInputs() }

            binding.registerPageLink.apply {
                paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
                setOnClickListener { navigationListener.navigateToRegister() }
            }

            return binding.root
        }

        private fun checkInputs() {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString()
            if (username.isEmpty() || password.isEmpty()) {
                DialogHelper.showSnacksbar(binding.root, "Please fill all the fields")
            } else {
                login(username, password)
            }

        }
        private fun login(username: String, password: String) {
            db = DBHelper.getDB(requireActivity())
            userDao = db.userDao()
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    val user = userDao.getUserByUsername(username)  // suspend call ✅

                    if (user == null) {
                        DialogHelper.showSnacksbar(binding.root, "User not found!")
                    } else {
                        val hashedInput = User.hashPassword(password)
                        if (hashedInput == user.passwordHash) {
                            // ✅ Login success
                            DialogHelper.showSnacksbar(binding.root, "Login Successful")

                            // Navigate to main activity
                            val intent = Intent(requireActivity(), MainActivity::class.java)
                            startActivity(intent)
                            requireActivity().finishAffinity()
                        } else {
                            // ❌ Wrong password
                            DialogHelper.showSnacksbar(binding.root, "Incorrect Password")
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    DialogHelper.showSnacksbar(binding.root, "Error: ${e.message}")
                }
            }
        }

    }


