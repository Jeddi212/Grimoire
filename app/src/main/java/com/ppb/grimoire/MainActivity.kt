package com.ppb.grimoire

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ppb.grimoire.db.NoteHelper
import com.ppb.grimoire.db.ScheduleHelper

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var ScHelp: ScheduleHelper
        lateinit var NtHelp: NoteHelper
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Initialize the bottom navigation view
        //create bottom navigation view object
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.notesFragment,
                R.id.todayFragment,
                R.id.scheduleFragment,
                R.id.newsFragment,
                R.id.profileFragment
            )
        )

        ScHelp = ScheduleHelper.getInstance(applicationContext)
        NtHelp = NoteHelper.getInstance(applicationContext)
        ScHelp.open()
        NtHelp.open()

        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu2 -> {
                val i = Intent(this, AboutActivity::class.java)
                startActivity(i)
                true
            }
            else -> true
        }
    }
}