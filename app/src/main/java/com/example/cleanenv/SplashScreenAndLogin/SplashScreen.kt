package com.example.cleanenv.SplashScreenAndLogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.cleanenv.R

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Looper.myLooper()?.let {
            Handler(it) .postDelayed({
                val intent = Intent(this, both::class.java)
                startActivity(intent)
                finish()
            }, 1000)
        }
    }
}