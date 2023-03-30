package com.example.cleanenv.ui.home


import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cleanenv.R
import com.example.cleanenv.Utils.Constants
import com.example.cleanenv.databinding.ActivityOrderSummeryBinding
import com.example.cleanenv.fullOrder
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class order_summery : AppCompatActivity() {
    val prefname = "myPref"
    var phone = ""
    var name = ""
    lateinit var sharedPreferences: SharedPreferences
    lateinit var binding: ActivityOrderSummeryBinding
    private lateinit var database: DatabaseReference
    private lateinit var progrsDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityOrderSummeryBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)
        progrsDialog = ProgressDialog(this)
        progrsDialog.show()
        sharedPreferences = this.getSharedPreferences(prefname, Context.MODE_PRIVATE)
        val recyclerview = findViewById<RecyclerView>(R.id.recyleViewProductList)
        phone = sharedPreferences.getString("phone", null).toString()
        database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://cleanenv-4ca72-default-rtdb.firebaseio.com/")

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        // ArrayList of class ItemsViewModel
        val data = ArrayList<ItemsViewModel>()
        val order= ArrayList<String>()

        // This loop will create 20 Views containing
        // the image with the count of view
        if(paper==1){
            data.add(ItemsViewModel("paper", Constants().paper_iron_steel))
            order.add("paper")
        }
        if(bottle==1){
            data.add(ItemsViewModel("bottle", Constants().galsses))
            order.add("bottle")
        }
        if(canMetal==1){
            data.add(ItemsViewModel("canMetal", Constants().paper_iron_steel))
            order.add("canMetal")
        }
        if(glassWine==1){
            data.add(ItemsViewModel("glassWine", Constants().galsses))
            order.add("glass")
        }
        if(cloths==1){
            data.add(ItemsViewModel("cloths", Constants().clothes_Others))
            order.add("clothes")
        }
        if(OthersTrush==1){
            data.add(ItemsViewModel("OthersTrush", Constants().clothes_Others))
            order.add("Others")
        }
//        Toast.makeText(this, "${paper} + ${bottle} + ${canMetal} + ${glassWine} + ${cloths} + ${OthersTrush}", Toast.LENGTH_SHORT).show()
        // This will pass the ArrayList to our Adapter
        val adapter = OrderAdapter(data)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter
        var address = "231/1,N.S.C Bose Road, Kolkata-700045"
        database.child("users").child(phone).get().addOnSuccessListener {
            if(it.value!=null){
                name = it.child("name").value.toString()
                if(it.child("bankDetails").child("acntNo").value.toString()!="null")binding.accountNo.setText(it.child("bankDetails").child("acntNo").value.toString())
                if(it.child("bankDetails").child("userName").value.toString()!="null")binding.AccountName.setText(it.child("bankDetails").child("userName").value.toString())
                if(it.child("bankDetails").child("ifscno").value.toString()!="null")binding.editIFSCNo.setText(it.child("bankDetails").child("ifscno").value.toString())
                address = it.child("address").value.toString()
                progrsDialog.dismiss()
            }
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
            Toast.makeText(this, "Error getting data from firebase", Toast.LENGTH_SHORT)
                .show()
            progrsDialog.dismiss()
        }

        binding.PlacingOrder.setOnClickListener {
            val key = database.child("orders").push().key
            val value = fullOrder(order,0,name,phone,"unassigned",false,"231/1,N.S.C Bose Road, Kolkata-700045")
            if (key != null) {
                database.child("orders").child(key).setValue(value).addOnSuccessListener {
                    Toast.makeText(this, "order placed", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "something wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

}