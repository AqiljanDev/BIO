package com.example.bio.presentation

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.bio.R
import com.example.bio.databinding.ActivityMainBinding
import com.example.bio.extension.defaultCustom
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
import com.example.data.SharedPreferencesManager
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val sharedPreference = SharedPreferencesManager.getInstance(this)

    val badgeGroup by lazy { binding.bottomNavigation.getOrCreateBadge(R.id.group) }
    val badgeFavorite by lazy { binding.bottomNavigation.getOrCreateBadge(R.id.favorite) }
    val badgeBasket by lazy { binding.bottomNavigation.getOrCreateBadge(R.id.basket) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.navView.setNavigationItemSelectedListener(this)

        realizeToolbar()
        realizeNavigationDrawerMenu()
        realizeBottomNavigation()

        badgeGroup.defaultCustom(horizontalSize = -4)
        badgeFavorite.defaultCustom(horizontalSize = -7)
        badgeBasket.defaultCustom(horizontalSize = -6)

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
            AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.quit)
                .setMessage(R.string.really_quit)
                .setPositiveButton(R.string.yes,
                    DialogInterface.OnClickListener { dialog, which ->
                        Log.d("Mylog", "Run onclick")
                        sharedPreference.removeString(SharedPreferencesManager.KEYS.TOKEN)
                        finish()
                    })
                .setNegativeButton(R.string.no, null)
                .show()
        }
    }

    fun replacerFragment(fragment: Fragment) {
        val fragmentTrans = supportFragmentManager.beginTransaction()
        fragmentTrans.replace(R.id.main_container, fragment)
        fragmentTrans.addToBackStack(null) // Добавляем транзакцию в back stack
        fragmentTrans.commit()
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

    fun hideBottomNavigation() {
        val slideOutAnimation: Animation =
            AnimationUtils.loadAnimation(this, R.anim.slide_out_bottom_navigation)
        binding.bottomNavigation.startAnimation(slideOutAnimation)
        binding.bottomNavigation.visibility =
            View.GONE // скрыть BottomNavigation после завершения анимации
    }

    fun showBottomNavigation() {
        val slideInAnimation =
            AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom_navigation)
        binding.bottomNavigation.startAnimation(slideInAnimation)
        binding.bottomNavigation.visibility =
            View.VISIBLE // показать BottomNavigation после завершения анимации
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Mylog", "On destroy Main activity")
    }

}
