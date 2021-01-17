package com.example.semesterproject

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

@Suppress("DEPRECATION")
class OpeningPage : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("LOGO", "WTF happened")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.opening_page)
        val logo = findViewById<ImageView>(R.id.logo)

        logo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_in))
        Handler().postDelayed({
            logo.startAnimation(AnimationUtils.loadAnimation(this,R.anim.anim_out))
            Handler().postDelayed({
                logo.visibility = View.GONE
                startActivity(Intent(this,MainLoginActivity::class.java))
                finish()
            },15000)
        },1500)

    }

}