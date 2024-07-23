package com.example.bio.presentation

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.animation.OvershootInterpolator
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.bio.R
import com.example.bio.databinding.ActivityMainBinding
import com.example.bio.presentation.base.BaseBottomFragment
import com.example.bio.presentation.basket.BasketFragment
import com.example.bio.presentation.category.CategoryFragment
import com.example.bio.presentation.compare.CompareFragment
import com.example.bio.presentation.favorite.FavoriteFragment
import com.example.bio.presentation.left_menu.CategoriesListFragment
import com.example.bio.presentation.left_menu.ContactsFragment
import com.example.bio.presentation.left_menu.DeliveryShippingFragment
import com.example.bio.presentation.left_menu.HelpSupportFragment
import com.example.bio.presentation.left_menu.StockFragment
import com.example.bio.presentation.profile.ProfileFragment
import com.example.bio.presentation.search.SearchFragment
import com.example.bio.utils.defaultCustom
import com.example.data.SharedPreferencesManager
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val sharedPreference by lazy { SharedPreferencesManager.getInstance(this) }

    val badgeGroup by lazy { binding.bottomNavigation.getOrCreateBadge(R.id.group) }
    val badgeFavorite by lazy { binding.bottomNavigation.getOrCreateBadge(R.id.favorite) }
    val badgeBasket by lazy { binding.bottomNavigation.getOrCreateBadge(R.id.basket) }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        enableEdgeToEdge()

        setSupportActionBar(binding.toolbar)
        binding.navView.setNavigationItemSelectedListener(this)

        realizeToolbar()
        realizeNavigationDrawerMenu()
        realizeBottomNavigation()

        badgeGroup.defaultCustom(horizontalSize = -4)
        badgeFavorite.defaultCustom(horizontalSize = -7)
        badgeBasket.defaultCustom(horizontalSize = -6)

        supportFragmentManager.addOnBackStackChangedListener {
            Log.d("Mylog", "Support fragment manager")
            updateBottomNavigation()
        }
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

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.catalog -> {
                    replacerFragment(CategoryFragment())
                }
                R.id.group -> {
                    badgeGroup.isVisible = false
                    replacerFragment(CompareFragment())
                }
                R.id.favorite -> {
                    badgeFavorite.isVisible = false
                    replacerFragment(FavoriteFragment())
                }
                R.id.basket -> {
                    badgeBasket.isVisible = false
                    replacerFragment(BasketFragment())
                }
                R.id.profile -> {
                    replacerFragment(ProfileFragment())
                }
            }
            true
        }

        binding.tvLogout.setOnClickListener {
            sharedPreference.removeString(SharedPreferencesManager.KEYS.TOKEN)
            finish()
        }
    }

    fun replacerFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val currentFragment = fragmentManager.findFragmentById(R.id.main_container)

        // Check if the fragment is the same type
        if (currentFragment?.javaClass == fragment.javaClass) return

        val fragmentTrans = fragmentManager.beginTransaction()
        if (fragment !is BaseBottomFragment) {
            fragmentTrans.setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        } else {
            fragmentTrans.setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out
            )
        }

        fragmentTrans.replace(R.id.main_container, fragment)
        fragmentTrans.addToBackStack(null)
        fragmentTrans.commitAllowingStateLoss()
    }

    private fun updateBottomNavigation() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.main_container)
        Log.d("Mylog", "open update cf = $currentFragment")
        val newSelectedItemId = when (currentFragment) {
            is CategoryFragment -> R.id.catalog
            is CompareFragment -> R.id.group
            is FavoriteFragment -> R.id.favorite
            is BasketFragment -> R.id.basket
            is ProfileFragment -> R.id.profile
            else -> null
        }

        // Check if the selected item is different
        if (newSelectedItemId != null && binding.bottomNavigation.selectedItemId != newSelectedItemId) {
            binding.bottomNavigation.selectedItemId = newSelectedItemId
        } else {
            Log.d("Mylog", "current fragment = $currentFragment")
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.catalog_submenu -> replacerFragment(CategoriesListFragment())
            R.id.price_sale -> replacerFragment(StockFragment())
            R.id.delivery_shipping -> replacerFragment(DeliveryShippingFragment())
            R.id.contacts -> replacerFragment(ContactsFragment())
            R.id.help_support -> replacerFragment(HelpSupportFragment())
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}




