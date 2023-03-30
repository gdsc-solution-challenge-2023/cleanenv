package com.example.cleanenv.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.cleanenv.databinding.ActivityAddressDetailsBinding

var paper = 0
var bottle = 0
var canMetal = 0
var glassWine = 0
var cloths = 0
var OthersTrush = 0
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

        paper = intent.getIntExtra("paper",0)
        bottle = intent.getIntExtra("bottle",0)
        canMetal = intent.getIntExtra("canMetal",0)
        glassWine = intent.getIntExtra("glassWine",0)
        cloths = intent.getIntExtra("cloths",0)
        OthersTrush = intent.getIntExtra("OthersTrush",0)
        binding.HomeCardView.setOnClickListener {
            val intent = Intent(this,order_summery::class.java)
            intent.putExtra("home", true)
            intent.putExtra("office", false)
            intent.putExtra("paper",paper)
            intent.putExtra("bottle",bottle)
            intent.putExtra("canMetal",canMetal)
            intent.putExtra("glassWine",glassWine)
            intent.putExtra("cloths",cloths)
            intent.putExtra("OthersTrush",OthersTrush)
            startActivity(intent)
        }
        binding.OfficeCardView.setOnClickListener {
            val intent = Intent(this,order_summery::class.java)
            intent.putExtra("home", false)
            intent.putExtra("office", true)
            intent.putExtra("paper",paper)
            intent.putExtra("bottle",bottle)
            intent.putExtra("canMetal",canMetal)
            intent.putExtra("glassWine",glassWine)
            intent.putExtra("cloths",cloths)
            intent.putExtra("OthersTrush",OthersTrush)
            startActivity(intent)
        }
//        Toast.makeText(this, "${paper} + ${bottle} + ${canMetal} + ${glassWine} + ${cloths} + ${OthersTrush}", Toast.LENGTH_SHORT).show()
    }

}