package com.example.cleanenv.EmpUi

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cleanenv.databinding.ActivityTrackOrderBinding

class TrackOrder : AppCompatActivity() {
    lateinit var binding: ActivityTrackOrderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackOrderBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)
        binding.map.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=22.5787419,88.4738648&mode=d"))
            intent.setPackage("com.google.android.apps.maps")
            startActivity(intent)
        }
        binding.continu.setOnClickListener {
            val select = intent.getStringExtra("key")
            val intent = Intent(this,AfterTrackOrder::class.java)
            intent.putExtra("key",select)
            startActivity(intent)
        }
    }
}