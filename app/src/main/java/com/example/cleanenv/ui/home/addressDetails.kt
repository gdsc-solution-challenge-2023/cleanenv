package com.example.cleanenv.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cleanenv.R
import com.example.cleanenv.databinding.ActivityAddressDetailsBinding

class addressDetails : AppCompatActivity() {
    lateinit var binding: ActivityAddressDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddressDetailsBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)
        binding.imgViewaddAddress.setOnClickListener{
            val intent = Intent(this,newAddress::class.java)
            startActivity(intent)
        }
    }
}