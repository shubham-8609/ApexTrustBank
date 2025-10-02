package com.codeleg.apextrustbank

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.codeleg.apextrustbank.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        initViews()
        setupToolbar()
        setupDrawer()
        setupNavigation()
        setupBackPressHandler()
        setupFragmentListener()

        if (savedInstanceState==null) replaceFragment(HomePageFragment())
    }

    // -------------------- Initialization --------------------
    private fun initViews() {
        drawerLayout = binding.main
        navigationView = binding.navigationView
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbarMainActivity)

    }

    // -------------------- Menu --------------------

    override fun onCreateOptionsMenu(menu: Menu) =
        menuInflater.inflate(R.menu.toolbar_menu, menu).let { true }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
         when (item.itemId) {
            R.id.home_button -> if (!isHomeFragmentVisible()) replaceFragment(HomePageFragment())
            R.id.exit_option -> finishAffinity()
            R.id.ask_question_option ->
                DialogHelper.sendEmail(this , binding.root , "Question from ApexTrustBank App" , "Hello Developers , [Your Quesry]")
            R.id.settings_option_toolbar -> replaceFragment(SettingsFragment(), true)
        }
        return true
    }

    // -------------------- Drawer & Navigation --------------------
    private fun setupDrawer() {
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            binding.toolbarMainActivity,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun setupNavigation() {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home_page_option -> {
                    replaceFragment(HomePageFragment(), false)
                    updateToolbarTitle()
                }
                R.id.transaction_option -> replaceFragment(TransactionFragment(), true)
                R.id.settings_option -> replaceFragment(SettingsFragment(), true)
            }
            menuItem.isChecked = true
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    // -------------------- Fragment Replacement --------------------
    private fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        val transaction = supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(
            android.R.anim.slide_in_left,
            android.R.anim.slide_out_right,
            android.R.anim.slide_in_left,
            android.R.anim.slide_out_right
        )
            replace(R.id.main_container, fragment)
            if (addToBackStack)  addToBackStack(null)
            commit()
        }
        updateToolbarTitle()
    }


    private fun updateToolbarTitle() {
        val currentFragment =
        supportActionBar?.title = when(supportFragmentManager.findFragmentById(R.id.main_container)) {
                is HomePageFragment -> "Dashboard"
                is TransactionFragment -> "Transaction History"
                is SettingsFragment -> "Settings"
                else -> "Apex Trust Bank"
        }
    }

    // -------------------- Back Press --------------------
    private fun setupBackPressHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when {
                    drawerLayout.isDrawerOpen(GravityCompat.START) ->
                        drawerLayout.closeDrawer(GravityCompat.START)

                    !isHomeFragmentVisible() -> {
                        supportFragmentManager.popBackStack(null,androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
                        replaceFragment(HomePageFragment(), false)
                        }
                    else -> finishAffinity()
                }
            }
        })
    }


    private fun isHomeFragmentVisible() =  supportFragmentManager.findFragmentById(R.id.main_container) is HomePageFragment


    private fun setupFragmentListener() {
        supportFragmentManager.addOnBackStackChangedListener {
            updateToolbarTitle()
        }
    }

}
