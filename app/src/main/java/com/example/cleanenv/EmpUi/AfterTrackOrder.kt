package com.example.cleanenv.EmpUi

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cleanenv.Utils.Constants
import com.example.cleanenv.databinding.ActivityAfterTrackOrderBinding
import com.example.cleanenv.ui.home.ItemsViewModel

class AfterTrackOrder : AppCompatActivity() {
    lateinit var binding: ActivityAfterTrackOrderBinding
    var total = 0
    var finalValue = 0
    var send = ArrayList<ItemsViewModel>()
    var value = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAfterTrackOrderBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)

        val ranks = Constants().ranks

        binding.btnPrice.setOnClickListener {
            binding.btnEmpContinue.visibility = View.VISIBLE
            finalValue = 0
            if(binding.edtTextPlastic.text.toString()!="")finalValue =binding.edtTextPlastic.text.toString().toInt()
            finalValue *= ranks.getValue(binding.checkbPlastic.text.toString())
            binding.txtViewPriceplastic.text = finalValue.toString()

            finalValue =0
            if(binding.edtTextMetal.text.toString()!="")finalValue =binding.edtTextMetal.text.toString().toInt()
            finalValue = finalValue * ranks.getValue(binding.checkbMetal.text.toString())
            binding.txtViewPriceMetal.text = finalValue.toString()

            finalValue =0
            if(binding.edttextGlass.text.toString()!="")finalValue =binding.edttextGlass.text.toString().toInt()
            finalValue = finalValue * ranks.getValue(binding.checkbglass.text.toString())
            binding.txtViewPriceGlass.text = finalValue.toString()

            finalValue =0
            if(binding.edtTextPaper.text.toString()!="")finalValue =binding.edtTextPaper.text.toString().toInt()
            finalValue = finalValue * ranks.getValue(binding.checkbPaper.text.toString())
            binding.txtViewPricePaper.text = finalValue.toString()

            finalValue =0
            if(binding.edtTextCloth.text.toString()!="")finalValue =binding.edtTextCloth.text.toString().toInt()
            finalValue = finalValue * ranks.getValue(binding.checkbCloth.text.toString())
            binding.txtViewPriceCloth.text = finalValue.toString()

            finalValue =0
            if(binding.edtTextUtensils.text.toString()!="")finalValue =binding.edtTextUtensils.text.toString().toInt()
            finalValue = finalValue * ranks.getValue(binding.checkbUtensils.text.toString())
            binding.txtViewPriceUtensils.text = finalValue.toString()

            finalValue =0
            if(binding.edtTextHardBind.text.toString()!="")finalValue =binding.edtTextHardBind.text.toString().toInt()
            finalValue = finalValue * ranks.getValue(binding.checkbHardBind.text.toString())
            binding.txtViewPriceHardBind.text = finalValue.toString()

            finalValue =0
            if(binding.edtTextBooks.text.toString()!="")finalValue =binding.edtTextBooks.text.toString().toInt()
            finalValue = finalValue * ranks.getValue(binding.checkbBooks.text.toString())
            binding.txtViewPriceBooks.text = finalValue.toString()
        }

        binding.btnEmpContinue.setOnClickListener {
            if(binding.checkbPlastic.isChecked) {
                total += binding.txtViewPriceplastic.text.toString().toInt()
                value.add(binding.checkbPlastic.text.toString())
                send.add(ItemsViewModel(binding.checkbPlastic.text.toString(),binding.txtViewPriceplastic.text.toString()))
            }
            if(binding.checkbMetal.isChecked) {
                total += binding.txtViewPriceMetal.text.toString().toInt()
                value.add(binding.checkbMetal.text.toString())
                send.add(ItemsViewModel(binding.checkbMetal.text.toString(),binding.txtViewPriceMetal.text.toString()))
            }
            if(binding.checkbglass.isChecked) {
                total += binding.txtViewPricePaper.text.toString().toInt()
                value.add(binding.checkbPaper.text.toString())
                send.add(ItemsViewModel(binding.checkbPaper.text.toString(),binding.txtViewPricePaper.text.toString()))
            }
            if(binding.checkbCloth.isChecked) {
                total += binding.txtViewPriceCloth.text.toString().toInt()
                value.add(binding.checkbCloth.text.toString())
                send.add(ItemsViewModel(binding.checkbCloth.text.toString(),binding.txtViewPriceCloth.text.toString()))
            }
            if(binding.checkbUtensils.isChecked) {
                total += binding.txtViewPriceUtensils.text.toString().toInt()
                value.add(binding.checkbUtensils.text.toString())
                send.add(ItemsViewModel(binding.checkbUtensils.text.toString(),binding.txtViewPriceUtensils.text.toString()))
            }
            if(binding.checkbHardBind.isChecked) {
                total += binding.txtViewPriceHardBind.text.toString().toInt()
                value.add(binding.checkbHardBind.text.toString())
                send.add(ItemsViewModel(binding.checkbHardBind.text.toString(),binding.txtViewPriceHardBind.text.toString()))
            }
            if(binding.checkbBooks.isChecked) {
                total += binding.txtViewPriceBooks.text.toString().toInt()
                value.add(binding.checkbBooks.text.toString())
                send.add(ItemsViewModel(binding.checkbBooks.text.toString(),binding.txtViewPriceBooks.text.toString()))
            }
            if(binding.checkbPaper.isChecked) {
                total += binding.txtViewPricePaper.text.toString().toInt()
                value.add(binding.checkbPaper.text.toString())
                send.add(ItemsViewModel(binding.checkbPaper.text.toString(),binding.txtViewPricePaper.text.toString()))
            }

            val select = intent.getStringExtra("key")
            val intent = Intent(this,lastOrderSummery::class.java)
            intent.putExtra("val",value)
//            intent.putExtra("data",send)
            intent.putExtra("total",total)
            intent.putExtra("key",select)
            startActivity(intent)
            Toast.makeText(this, total.toString(), Toast.LENGTH_SHORT).show()
            total = 0
        }
    }
}