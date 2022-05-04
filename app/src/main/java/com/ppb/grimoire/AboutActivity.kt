package com.ppb.grimoire

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.lang.String
import java.util.*

import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element;

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val adsElement = Element()
        val aboutPage: View = AboutPage(this)
            .isRTL(false)
            .setImage(R.drawable.ic_grimoire_foreground)
            .setDescription("Grimoire is a note taking app with the ability to mix other media to be noted (audio, image, file)")
            .addItem(Element().setTitle("Version 1.0"))
            .addGroup("CONNECT WITH US!")
            .addEmail("grimoireppb@gmail.com")
            .addWebsite("Your website/")
            .addYoutube("UCRm8ZV7WY7x3SUrzJdjXkQA")
            .addGitHub("Jeddi212/Grimoire")
            .addInstagram("jedediah_jeddi")
            .addItem(createCopyright())
            .create()
        setContentView(aboutPage)
    }

    private fun createCopyright(): Element? {
        val copyright = Element()
        @SuppressLint("DefaultLocale") val copyrightString =
            String.format("Copyright %d by Grimoire", Calendar.getInstance().get(Calendar.YEAR))
        copyright.setTitle(copyrightString)
        copyright.setGravity(Gravity.CENTER)
        copyright.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@AboutActivity, copyrightString, Toast.LENGTH_SHORT).show()
            }
        })
        return copyright
    }
}