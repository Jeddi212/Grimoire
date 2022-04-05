package com.ppb.grimoire

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity(), View.OnClickListener {

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
    lateinit var mGoogleSignInClient : GoogleSignInClient
    lateinit var signout : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lateinit var user : User
        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null) {
            var personName = acct.displayName.toString()
            var personGivenName = acct.givenName.toString()
            var personFamilyName = acct.familyName.toString()
            var personEmail = acct.email.toString()
            var personId = acct.id.toString()
            var personPhoto = acct.photoUrl
            Log.d("name",personName)
            user = User(personName,personGivenName,personFamilyName,personEmail,personId, personPhoto)
        }
        Log.d("name",user.personName)

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

        signout = findViewById(R.id.sign_out_button)
        signout.setOnClickListener(this)

        bottomNavigationView.setupWithNavController(navController)

    }
    private fun signOut() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this) {
                Toast.makeText(this,"Signed Out Successfully", Toast.LENGTH_LONG).show()
                finish()
            }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.sign_out_button-> signOut()
        }
    }

}