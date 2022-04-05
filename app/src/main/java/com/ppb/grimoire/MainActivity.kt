package com.ppb.grimoire

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.ppb.grimoire.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

//    private lateinit var binding: ActivityMainBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val navView: BottomNavigationView = binding.navView
//
//        val navController = findNavController(R.id.nav_host_fragment_activity_main)
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Initialize the bottom navigation view
        //create bottom navigation view object
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        // Dynamic Action Bar, Buang aja kali ya? pasang textview manual aja nanti?
        // Soalnya nutupin content
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.notesFragment,
                R.id.todayFragment,
                R.id.scheduleFragment,
                R.id.newsFragment,
                R.id.profileFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        bottomNavigationView.setupWithNavController(navController)
    }
}