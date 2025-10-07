package com.codeleg.apextrustbank

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.codeleg.apextrustbank.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() , UserValueUpdateListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var rootLayout: CoordinatorLayout
    private lateinit var navigationView: BottomNavigationView
    private lateinit var db: DBHelper
    private lateinit var userDao: UserDao
    private lateinit var currentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        initViews()
        setupToolbar()
        setupNavigation()
        applyUserValues(savedInstanceState)
        setupBackPressHandler()
        setupFragmentListener()
    }

    private fun initViews() {
        rootLayout = binding.main
        navigationView = binding.navigationView
        db = DBHelper.getDB(this)
        userDao = db.userDao()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbarMainActivity)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }




    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home_button -> if (!isHomeFragmentVisible()) {
                if (::currentUser.isInitialized) {
                    replaceFragment(HomePageFragment.newInstance(
                        "₹ ${currentUser.balance}",
                        "Acc No: ${currentUser.accountNo}",
                        currentUser.username,
                        currentUser.id
                    ))
                } else {
                    replaceFragment(HomePageFragment())
                }
            }
            R.id.exit_option -> showExitDialog()
            R.id.ask_question_option ->
                DialogHelper.sendEmail(this, binding.root, "Question from ApexTrustBank App", "Hello Developers , [Your Query]")
            R.id.settings_option_toolbar -> replaceFragment(SettingsFragment(), true)
        }
        return true
    }

    private fun setupNavigation() {
        navigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home_page_option -> {
                    if (::currentUser.isInitialized) {
                        replaceFragment(HomePageFragment.newInstance(
                            "₹ ${currentUser.balance}",
                            "Acc No: ${currentUser.accountNo}",
                            currentUser.username,
                            currentUser.id
                        ), false)
                    } else {
                        replaceFragment(HomePageFragment(), false)
                    }
                    updateToolbarTitle()
                }
                R.id.transaction_option -> replaceFragment(TransactionFragment(), true)
                R.id.settings_option -> replaceFragment(SettingsFragment(), true)
                R.id.logout_option -> showLogoutDialog()
            }
            menuItem.isChecked = true
            true
        }
    }

    private fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right,
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right
            )
            replace(R.id.main_container, fragment)
            if (addToBackStack) addToBackStack(null)
            commit()
        }
        updateToolbarTitle()
    }

    private fun updateToolbarTitle() {
        supportActionBar?.title = when (supportFragmentManager.findFragmentById(R.id.main_container)) {
            is HomePageFragment -> "Dashboard"
            is TransactionFragment -> "Transaction History"
            is SettingsFragment -> "Settings"
            else -> "Apex Trust Bank"
        }
    }

    private fun setupBackPressHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when {
                    !isHomeFragmentVisible() -> {
                        supportFragmentManager.popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
                        if (::currentUser.isInitialized) {
                            replaceFragment(HomePageFragment.newInstance(
                                "₹ ${currentUser.balance}",
                                "Acc No: ${currentUser.accountNo}",
                                currentUser.username,
                                currentUser.id
                            ), false)
                        } else {
                            replaceFragment(HomePageFragment(), false)
                        }
                    }
                    else -> showExitDialog()
                }
            }
        })
    }

    private fun isHomeFragmentVisible(): Boolean =
        supportFragmentManager.findFragmentById(R.id.main_container) is HomePageFragment

    private fun setupFragmentListener() {
        supportFragmentManager.addOnBackStackChangedListener {
            updateToolbarTitle()
        }
    }

    private fun applyUserValues(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) return
        val userId = intent.getIntExtra("USER_ID", -1)
        if (userId != -1) {
            lifecycleScope.launch(Dispatchers.IO) {
                currentUser = userDao.getUserById(userId)!!
                launch(Dispatchers.Main) {
                    replaceFragment(HomePageFragment.newInstance(
                        "₹ ${currentUser.balance}",
                        "Acc No: ${currentUser.accountNo}",
                        currentUser.username,
                        currentUser.id
                    ))
                }
            }
        } else {
            replaceFragment(HomePageFragment())
        }
    }



    private fun showExitDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Exit App")
        builder.setMessage("Are you sure you want to exit?")
        builder.setPositiveButton("Yes") { dialog, _ ->
            dialog.dismiss()
            finishAffinity() // ✅ Closes all activities
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        builder.setCancelable(true)
        builder.show()
    }
    private fun showLogoutDialog(){
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setTitle("Log out!")
            setMessage("Are you sure you  want to log out ?")
            setPositiveButton("Yes") {dialog , _ ->
                logoutLogic()
            }
            setNegativeButton("No") {dialog, which ->
                dialog.dismiss()
            }.show()
        }
    }

    private fun logoutLogic(){
        PrefsManager.logout(this)
        val intent = Intent(applicationContext, AuthenticationActivity()::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onValueChanged() {
        applyUserValues(null)
    }


}
