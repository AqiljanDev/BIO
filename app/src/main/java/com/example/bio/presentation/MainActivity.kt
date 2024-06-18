package com.example.bio.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.bio.R
import com.example.bio.data.api.retrofitCheckToken
import com.example.bio.databinding.ActivityMainBinding
import com.example.bio.presentation.category.CategoryFragment
import com.example.bio.presentation.search.SearchFragment
import com.example.data.SharedPreferencesManager
import com.example.login.LoginActivity
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        checkToken()

        setSupportActionBar(binding.toolbar)
        binding.navView.setNavigationItemSelectedListener(this)


        realizeToolbar()
        realizeNavigationDrawerMenu()
        realizeBottomNavigation()
    }

    private fun realizeToolbar() {
        binding.imageViewSearch.setOnClickListener {
            replacerFragment(SearchFragment())
        }
    }

    private fun realizeNavigationDrawerMenu() {
        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar, R.string.open_nav, R.string.close_nav
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun realizeBottomNavigation() {
        replacerFragment(CategoryFragment())

        binding.bottomNavigation.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.catalog -> {
                    replacerFragment(CategoryFragment())
                }
                R.id.group -> {
                    // Handle group item
                }
                R.id.basket -> {
                    // Handle basket item
                }
                R.id.profile -> {
                    // Handle profile item
                }
            }
            true
        }
    }

    private fun checkToken() {
        lifecycleScope.launch {
            val sharedPreferences = SharedPreferencesManager.getInstance(baseContext)
            val token = sharedPreferences.getString(SharedPreferencesManager.KEYS.TOKEN)

            try {
                Log.d("Mylog", "Token = $token")
                retrofitCheckToken.testGet(token)
            } catch (ex: Exception) {
                Log.d("Mylog", "Exception main activity = ${ex.message}")
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun replacerFragment(fragment: Fragment) {
        val fragmentTrans = supportFragmentManager.beginTransaction()
        fragmentTrans.replace(R.id.main_container, fragment)
        fragmentTrans.commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> Toast.makeText(this, "Search click", Toast.LENGTH_SHORT).show()
            // Handle other menu items
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
