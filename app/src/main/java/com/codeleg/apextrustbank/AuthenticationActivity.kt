package com.codeleg.apextrustbank

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.codeleg.apextrustbank.databinding.ActivityAuthenticationBinding

class AuthenticationActivity : AppCompatActivity(), AuthenticationListener {
    private lateinit var binding: ActivityAuthenticationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        if (PrefsManager.isLoggedIn(this)) {

            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("USER_ID", PrefsManager.getUserId(this@AuthenticationActivity))
            }
            startActivity(intent)
            finish()
        }



        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (savedInstanceState == null) replaceFragment(SignInFragment())
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun navigateToSignIn() {
        replaceFragment(SignInFragment())
    }

    override fun navigateToRegister() {
        replaceFragment(RegisterFragment())
    }


}
