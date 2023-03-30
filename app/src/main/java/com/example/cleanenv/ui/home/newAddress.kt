package com.example.cleanenv.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cleanenv.databinding.ActivityNewAddressBinding

class newAddress : AppCompatActivity() {
    lateinit var binding : ActivityNewAddressBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewAddressBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)



        binding.continuee.setOnClickListener {
            finish()
        }
    }


}