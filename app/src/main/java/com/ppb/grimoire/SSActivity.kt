package com.ppb.grimoire

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SSActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed(
            { //This method will be executed once the timer is over
                // Start your app main activity
                val i = Intent(this, LoginActivity::class.java)
                startActivity(i)

                // Close this activity
                finish()
            }, 4500
        )
    }
}