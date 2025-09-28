package com.codeleg.apextrustbank

import android.content.Intent
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
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

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
        setupToolbar()
        setupDrawer()
        setupNavigation()
        setupBackPressHandler()
        setupFragmentListener()

        if (savedInstanceState == null) {
            replaceFragment(HomePageFragment())
        }
    }

    // -------------------- Initialization --------------------
    private fun initViews() {
        drawerLayout = binding.main
        navigationView = binding.navigationView
        toolbar = binding.toolbarMainActivity
    }

    // -------------------- Toolbar --------------------
    private fun setupToolbar() {
        setSupportActionBar(toolbar)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
         when (item.itemId) {
            R.id.home_button -> {
                if (!isHomeFragmentVisible()) {
                    replaceFragment(HomePageFragment(), false) // don’t add Home to back stack
                }
                true
            }
            R.id.exit_option -> finishAffinity()
            R.id.ask_question_option -> {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = android.net.Uri.parse("mailto:") // Only email apps should handle this
                    putExtra(Intent.EXTRA_EMAIL , arrayOf("shubhamgupta8609@gmail.com"))
                    putExtra(Intent.EXTRA_SUBJECT , "Question from Apex Trust Bank")
                    putExtra(Intent.EXTRA_TEXT , "Hello, developers ,[Your Query]")
                    setPackage("com.google.android.gm") // Target Gmail specifically
                }
                try {
                startActivity(intent)
                } catch (e:Exception){
                    Snackbar.make(binding.root, "No proper app to send mail", Snackbar.LENGTH_LONG).show()


                }
            }
             R.id.settings_option_toolbar -> replaceFragment(SettingsFragment(), true)

        }
        return super.onOptionsItemSelected(item)

    }



    // -------------------- Drawer --------------------
    private fun setupDrawer() {
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    // -------------------- Navigation --------------------
    private fun setupNavigation() {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home_page_option -> replaceFragment(HomePageFragment(), false)
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
        val transaction = supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                android.R.anim.slide_in_left,   // enter
                android.R.anim.slide_out_right, // exit
                android.R.anim.slide_in_left,   // popEnter
                android.R.anim.slide_out_right  // popExit
            )
            .replace(R.id.main_container, fragment)

        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    // -------------------- Back Press --------------------
    private fun setupBackPressHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when {
                    drawerLayout.isDrawerOpen(GravityCompat.START) -> {
                        drawerLayout.closeDrawer(GravityCompat.START)
                    }
                    !isHomeFragmentVisible() -> {
                        // Clear back stack
                        supportFragmentManager.popBackStack(
                            null,
                            androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
                        )
                        // Show Home fragment
                        replaceFragment(HomePageFragment(), false)
                    }
                    else -> {

                        finishAffinity()
                    }
                }
            }
        })
    }


    private fun isHomeFragmentVisible(): Boolean {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.main_container)
        return currentFragment is HomePageFragment
    }
    // -------------------- Fragment Listener --------------------
    private fun setupFragmentListener() {
        supportFragmentManager.addOnBackStackChangedListener {
            updateToolbarTitle()
        }
    }

    private fun updateToolbarTitle() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.main_container)
        val title = when (currentFragment) {
            is HomePageFragment -> "Dashboard"
            is TransactionFragment -> "Transaction History"
            is SettingsFragment -> "Settings"
            else -> "Apex Trust Bank"
        }
        supportActionBar?.title = title
    }
}
