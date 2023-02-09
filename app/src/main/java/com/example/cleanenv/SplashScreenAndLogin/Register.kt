package com.example.cleanenv.SplashScreenAndLogin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.util.Patterns.EMAIL_ADDRESS
import android.view.View
import android.widget.Toast
import androidx.core.util.PatternsCompat.EMAIL_ADDRESS
import com.example.cleanenv.R
import com.example.cleanenv.Utils.Registrationeed
import com.example.cleanenv.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.regex.Pattern

class Register : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth : FirebaseAuth
    lateinit var binding: ActivityRegisterBinding
    val checkPass = Registrationeed()
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

//        NameFocusListener()
//        PhoneFocusListener()
//        EmailFocusListener()
//        passwordFocusListener()

//
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://verdant-volt-default-rtdb.firebaseio.com/")
//
        binding.RegisterButton.setOnClickListener{
            binding.prog.visibility = View.VISIBLE
            name = binding.regName.text.toString()
            phone = binding.regPhone.text.toString()
            password = binding.regPass.text.toString()
//            confirmFocusListener()
            database.child("users").child(phone.toString()).get().addOnSuccessListener() {
                if (it.value != null) {
                    Toast.makeText(this, "this user id is already registered", Toast.LENGTH_SHORT)
                        .show()
                }else{
                    if (name == "" || phone == "" || email == "" || password == "" || conPass.toString() == "") {
                        Toast.makeText(this, "please enter all the details", Toast.LENGTH_SHORT).show()
                    }
                    else if(allOk()){
                        val User = user(name, phone, email,password)
                        phone?.let {
                            database.child("users").child(it).setValue(User).addOnSuccessListener {
                                binding.regName.text?.clear()
                                binding.regPhone.text?.clear()
                                binding.regPass.text?.clear()
                                Toast.makeText(this, "succesfuly saved", Toast.LENGTH_SHORT).show()
                            }.addOnFailureListener {
                                Toast.makeText(this, "dalised", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    else Toast.makeText(this, "please enter all the details properly", Toast.LENGTH_SHORT).show();
                    binding.prog.visibility = View.GONE
                }
            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
                Toast.makeText(this, "Error getting data from firebase", Toast.LENGTH_SHORT).show()
            }

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

//    private fun confirmFocusListener() {
//        binding.regPassCon.setOnFocusChangeListener { _, focused ->
//            if(!focused)
//            {
//                if (binding.regPass.text.toString() != binding.regPassCon.text.toString()) {
//                    binding.regPassConfirmHelper.helperText = "please enter same password in both"
//                }else binding.regPassConfirmHelper.helperText = null
//            }
//        }
//    }


    private fun allOk(): Boolean {
        if(binding.regNameHelper.helperText ==null && binding.regPhoneHelper.helperText == null && binding.regPassHelper.helperText == null )return true
        return false
    }

//    private fun NameFocusListener()
//    {
//        binding.regName.setOnFocusChangeListener { _, focused ->
//            if(!focused)
//            {
//                if(binding.regName.text.toString()!="")binding.regNameHelper.helperText = null
//            }
//        }
//    }
//    private fun PhoneFocusListener()
//    {
//        binding.regPhone.setOnFocusChangeListener { _, focused ->
//            if(!focused)
//            {
//                if(checkPass.isValidMobile(binding.regPhone.text.toString()))binding.regPhoneHelper.helperText = null
//                else binding.regPhoneHelper.helperText = "Please enter a valid phone number"
//            }
//        }
//    }
//    private fun EmailFocusListener()
//    {
//        binding.regEmail.setOnFocusChangeListener { _, focused ->
//            if(!focused)
//            {
//                if(checkPass.isEmailValid(binding.regEmail.text.toString()))binding.regEmailHelper.helperText = null
//                else binding.regEmailHelper.helperText = "Please enter a valid Email id"
//            }
//        }
//    }
    private fun passwordFocusListener()
    {
        binding.regPass.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.regPassHelper.helperText = checkPass.validPassword(binding.regPass.text.toString())
            }
        }
    }
//
//
}

//    public override fun onStart() {
//        super.onStart()
//        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = auth.currentUser
//        if(currentUser != null){
//            reload()
//        }
