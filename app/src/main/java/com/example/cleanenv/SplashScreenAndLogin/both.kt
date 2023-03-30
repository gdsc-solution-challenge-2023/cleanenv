package com.example.cleanenv.SplashScreenAndLogin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cleanenv.EmpUi.MainActivityEmp
import com.example.cleanenv.MainActivity
import com.example.cleanenv.databinding.ActivityBothBinding


class both : AppCompatActivity() {
    val prefname = "myPref"
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var editer: SharedPreferences.Editor
    lateinit var binding : ActivityBothBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBothBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPreferences = this.getSharedPreferences(prefname, Context.MODE_PRIVATE)
        editer = sharedPreferences.edit()
        editer.apply() {
            putBoolean("fst", false)
            apply()
        }
        binding.logInAsCustomer.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            intent.putExtra("fromCus",true)
            startActivity(intent)
        }
        binding.logInAsEmployee.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            intent.putExtra("fromCus",false)
            startActivity(intent)
        }
    }


    override fun onStart() {
        super.onStart()
        if(sharedPreferences.getBoolean("fst",false))finish()
        else{
            if (sharedPreferences.getBoolean("loggedin", false)) {
                if(sharedPreferences.getBoolean("custOrEmp", false)) {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("phone", sharedPreferences.getString("phone", null))
                    intent.putExtra("commingFromLogin", true)
                    startActivity(intent)
                    finish()
                }else{
                    val intent = Intent(this, MainActivityEmp::class.java)
                    intent.putExtra("email", sharedPreferences.getString("email", null))
                    intent.putExtra("commingFromLogin", true)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}