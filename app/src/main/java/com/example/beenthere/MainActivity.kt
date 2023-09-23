package com.example.beenthere

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.beenthere.databinding.ActivityMainBinding
import com.example.beenthere.ext.getVmFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    val viewModel by viewModels<MainViewModel> { getVmFactory() }

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this



        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navBottomView: BottomNavigationView = findViewById(R.id.bottom_nav)
        val navController = navHostFragment.navController
        navBottomView.setupWithNavController(navController)

        setUpToolbar()


    }


    private fun setUpToolbar() {

        val bottomNav = binding.bottomNav
        val color = Color.parseColor("#AE5745")
        val homeColor = Color.parseColor("#E0806C")
        findNavController(R.id.nav_host_fragment).addOnDestinationChangedListener { navController: NavController, _: NavDestination, _: Bundle? ->
            when (navController.currentDestination?.id) {



                R.id.homeFragment -> {
                    viewModel.isHome.value = true
                    bottomNav.setBackgroundResource(R.drawable.main_theme_color)
                }
                R.id.profileFragment -> {
                    viewModel.isHome.value = false
                    bottomNav.setBackgroundResource(R.drawable.gradient_border_2)
                }
                else -> {
                    viewModel.isHome.value = false
                    bottomNav.setBackgroundResource(R.drawable.main_theme_color)
                }

            }
        }
    }

}