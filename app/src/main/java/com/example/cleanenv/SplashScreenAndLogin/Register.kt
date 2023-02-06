package com.example.cleanenv.SplashScreenAndLogin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.cleanenv.R
import com.example.cleanenv.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Register : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth : FirebaseAuth
    lateinit var binding: ActivityRegisterBinding
    var phone :String? = null
    var name: String? =null
    var email:String?=null
    var password:String?=null
    var conPass:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://verdant-volt-default-rtdb.firebaseio.com/")

        binding.RegisterButton.setOnClickListener{
            binding.prog.visibility = View.VISIBLE
            name = binding.regName.text.toString()
            phone = binding.regPhone.text.toString()
            email = binding.regEmail.text.toString()
            password = binding.regPass.text.toString()
            conPass = binding.regPassCon.text.toString()
            if (name == "" || phone == "" || email == "" || password == "" || conPass.toString() == "") {
                Toast.makeText(this, "please enter all the details", Toast.LENGTH_SHORT).show()
            } else if (password != conPass.toString()) {
//                binding.regPass.error = "hi sexy"
                Toast.makeText(this, "please enter same pass in both", Toast.LENGTH_SHORT).show();
            }
            else{
                val User = user(name, phone, email,password)
                phone?.let {
                    database.child("users").child(it).setValue(User).addOnSuccessListener {
                        binding.regName.text?.clear()
                        binding.regPhone.text?.clear()
                        binding.regEmail.text?.clear()
                        binding.regPass.text?.clear()
                        binding.regPassCon.text?.clear()
                        Toast.makeText(this, "succesfuly saved", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(this, "dalised", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            binding.prog.visibility = View.GONE
        }


//            database.child("user").addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    if (snapshot.hasChild(phone)) Toast.makeText(
//                        this@singIn,
//                        "phone is already registered",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    else {
//                        database.child("user").child(phone).child("fullName").setValue(name)
//                        database.child("user").child(phone).child("email").setValue(email)
//                        database.child("user").child(phone).child("password").setValue(password)
//
//                        Toast.makeText(
//                            this@singIn,
//                            "user registered succewsully",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        finish()
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                }
//            })

        binding.btnLogRegister.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right)
    }


}

//    public override fun onStart() {
//        super.onStart()
//        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = auth.currentUser
//        if(currentUser != null){
//            reload()
//        }
//    }
//}
