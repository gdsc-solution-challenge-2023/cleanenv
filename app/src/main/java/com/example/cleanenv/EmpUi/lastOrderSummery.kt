package com.example.cleanenv.EmpUi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cleanenv.R
import com.example.cleanenv.Utils.Constants
import com.example.cleanenv.databinding.ActivityLastOrderSummeryBinding
import com.example.cleanenv.ui.home.ItemsViewModel
import com.example.cleanenv.ui.home.OrderAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class lastOrderSummery : AppCompatActivity() {
    lateinit var binding : ActivityLastOrderSummeryBinding
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLastOrderSummeryBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)

        val ranks = Constants().ranks
        val recyclerview = findViewById<RecyclerView>(R.id.recyleViewProductList)
        recyclerview.layoutManager = LinearLayoutManager(this)
        val value = intent.getStringArrayListExtra("val")
        val total = intent.getIntExtra("total",0)
        val key = intent.getStringExtra("key")
        val data = ArrayList<ItemsViewModel>()
        database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://cleanenv-4ca72-default-rtdb.firebaseio.com/")
        binding.totall.text = total.toString()
        if (value != null) {
            for (i in value){
                data.add(ItemsViewModel(i,ranks.get(i).toString() + "/kg"))
            }
        }
//        val data = intent.getSerializableExtra("data") as? ArrayList<ItemsViewModel>
        val adapter = data?.let { OrderAdapter(it) }

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter

        binding.btnEmpContinue.setOnClickListener {
            if (key != null) {
                database.child("orders").child(key).child("order").setValue(value).addOnSuccessListener {
                    Toast.makeText(this, "order completed", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "something wrong", Toast.LENGTH_SHORT).show()
                }
                database.child("orders").child(key).child("done").setValue(true).addOnSuccessListener {
                    Toast.makeText(this, "order completed", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "something wrong", Toast.LENGTH_SHORT).show()
                }
                database.child("orders").child(key).child("money").setValue(total).addOnSuccessListener {
                    Toast.makeText(this, "order completed", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "something wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}