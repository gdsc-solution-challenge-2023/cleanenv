package com.example.cleanenv.ui.home


import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cleanenv.R
import com.example.cleanenv.Utils.Constants
import com.example.cleanenv.databinding.ActivityAddressDetailsBinding
import com.example.cleanenv.databinding.ActivityOrderSummeryBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class order_summery : AppCompatActivity() {
    val prefname = "myPref"
    var phone = ""
    lateinit var sharedPreferences: SharedPreferences
    lateinit var binding: ActivityOrderSummeryBinding
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityOrderSummeryBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)

        sharedPreferences = this.getSharedPreferences(prefname, Context.MODE_PRIVATE)
        val recyclerview = findViewById<RecyclerView>(R.id.recyleViewProductList)
        phone = sharedPreferences.getString("phone", null).toString()
        database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://verdant-volt-default-rtdb.firebaseio.com/").child("users")

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        // ArrayList of class ItemsViewModel
        val data = ArrayList<ItemsViewModel>()

        // This loop will create 20 Views containing
        // the image with the count of view
        if(paper==1)data.add(ItemsViewModel("paper", Constants().paper_iron_steel))
        if(bottle==1)data.add(ItemsViewModel("bottle", Constants().galsses))
        if(canMetal==1)data.add(ItemsViewModel("canMetal", Constants().paper_iron_steel))
        if(glassWine==1)data.add(ItemsViewModel("glassWine", Constants().galsses))
        if(cloths==1)data.add(ItemsViewModel("cloths", Constants().clothes_Others))
        if(OthersTrush==1)data.add(ItemsViewModel("OthersTrush", Constants().clothes_Others))
        Toast.makeText(this, "${paper} + ${bottle} + ${canMetal} + ${glassWine} + ${cloths} + ${OthersTrush}", Toast.LENGTH_SHORT).show()
        // This will pass the ArrayList to our Adapter
        val adapter = OrderAdapter(data)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter

        database.child(phone).child("bankDetails").get().addOnSuccessListener {
            if(it.value!=null){
                binding.accountNo.setText(it.child("acntNumber").value.toString())
                binding.AccountName.setText(it.child("holderName").value.toString())
                binding.editIFSCNo.setText(it.child("IFSCcode").value.toString())
            }
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
            Toast.makeText(this, "Error getting data from firebase", Toast.LENGTH_SHORT)
                .show()
        }

    }

}