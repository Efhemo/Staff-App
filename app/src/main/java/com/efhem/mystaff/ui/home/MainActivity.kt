package com.efhem.mystaff.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.efhem.mystaff.R
import com.efhem.mystaff.databinding.ActivityMainBinding
import java.util.HashSet

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val bottomNavigationView = binding.bottomNavigation
        val appToolbar = binding.toolbar
        setSupportActionBar(appToolbar)
        //appToolbar.setTitleTextColor(resources.getColor(R.color.white))

        val topLevel = HashSet<Int>()
        topLevel.add(R.id.myStaffFragment)
        topLevel.add(R.id.notification)
        topLevel.add(R.id.addStaff)
        topLevel.add(R.id.scheduleFragment)
        topLevel.add(R.id.profileFragment)

        //Setting up toolbar, appbarconf, toplevel with nav takes care of the upbutton
        appBarConfiguration = AppBarConfiguration.Builder(topLevel).build()
        navController = Navigation.findNavController(this, R.id.fragment_host)
        NavigationUI.setupWithNavController(appToolbar, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)

    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

}
