package com.codeleg.apextrustbank

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.codeleg.apextrustbank.databinding.ActivityMainBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        initViews()
        setupToolbarAndDrawer()
        setupNavigation()

        if (savedInstanceState == null) {
            replaceFragment(HomePageFragment())
        }

        // Back press handling
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    finish() // Exit app if drawer not open
                }
            }
        })
    }

    private fun initViews() {
        drawerLayout = binding.main
        navigationView = binding.navigationView
        toolbar = findViewById(R.id.toolbar_main_activity)
    }

    private fun setupToolbarAndDrawer() {
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.open_drawer, R.string.close_drawer
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

    }

    private fun setupNavigation() {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home_page_option -> replaceFragment(HomePageFragment())
                R.id.transaction_option -> replaceFragment(TransactionFragment())
                R.id.settings_option -> replaceFragment(SettingsFragment()
                )
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .commit()
    }
}
