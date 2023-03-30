package com.example.cleanenv.SplashScreenAndLogin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.cleanenv.EmpUi.MainActivityEmp
import com.example.cleanenv.MainActivity
import com.example.cleanenv.SplashScreenAndLogin.both
import com.example.cleanenv.R
import com.example.cleanenv.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.cleanenv.Utils.Registrationeed

class Login : AppCompatActivity() {
    val prefname = "myPref"
    lateinit var phoneAfterGmailSignIn: String
    lateinit var sharedPreferences: SharedPreferences
    val checkPass = Registrationeed()
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var editer: SharedPreferences.Editor
    private lateinit var googleSignInClient: GoogleSignInClient
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
            .getReferenceFromUrl("https://cleanenv-4ca72-default-rtdb.firebaseio.com/")

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.btnSignUp.setOnClickListener {
            if(intent.getBooleanExtra("fromCus",false)==true)startActivity(Intent(this, Register::class.java))
            else Toast.makeText(this, "have to integrate the application section", Toast.LENGTH_SHORT).show()
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        }
//        passwordFocusListener()
//        PhoneFocusListener()

        if(intent.getBooleanExtra("fromCus",false)==false){
            binding.logphone.setHint("Email")
            binding.textView6.text = "Are you looking for the job?"
            binding.btnSignUp.text = "Apply"
            binding.googleSignIn.visibility = View.GONE
        }
        sharedPreferences = this.getSharedPreferences(prefname, Context.MODE_PRIVATE)
        editer = sharedPreferences.edit()
        binding.logbutton.setOnClickListener {
            binding.progLogIn.visibility = View.VISIBLE
            var phone = binding.logphone.text.toString()
            var password = binding.logpass.text.toString()
            if (phone.toString() == "" || password.toString() == "") {
                Toast.makeText(this, "please enter all the details", Toast.LENGTH_SHORT).show()
            } else if (!checkPass.isValidMobile(phone.toString()) && (intent.getBooleanExtra("fromCus",false)==true)) {
                Toast.makeText(this, "please enter a proper number", Toast.LENGTH_SHORT).show()
            } else {
                if(intent.getBooleanExtra("fromCus",false)==true){
                    phone = checkPass.isValidMobileAgainstIndian(phone).toString()
                    database.child("users").child(phone).get().addOnSuccessListener() {
                        if (it.value == null) {
                            Toast.makeText(
                                this,
                                "this phone number is not registered",
                                Toast.LENGTH_SHORT
                            ).show()
                        }else{
                            database.child("users").child(phone.toString()).get().addOnSuccessListener() {
                                if (it.child("password").value == password.toString()) {
                                    saveToSharedPrefrenceAndMoveToMainActivity(phone.toString())
                                } else if (it.child("password").value == "") {
                                    Toast.makeText(
                                        this,
                                        "May your acccount registred with google please sign in with google",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(this, "please enter proper password", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }.addOnFailureListener {
                                Log.e("firebase", "Error getting data", it)
                                Toast.makeText(this, "Error getting data from firebase", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }.addOnFailureListener {
                        Log.e("firebase", "Error getting data", it)
                        Toast.makeText(this, "Error getting data from firebase", Toast.LENGTH_SHORT)
                            .show()
                    }
                }else{
                    val mail = checkPass.mailDotNotTakingProblemSolved(phone).toString()
                    database.child("emp").child(mail).get().addOnSuccessListener() {
                        if (it.value == null) {
                            Toast.makeText(
                                this,
                                "this mail number is not registered",
                                Toast.LENGTH_SHORT
                            ).show()
                        }else{
                            database.child("emp").child(mail.toString()).get().addOnSuccessListener() {
                                if (it.child("password").value.toString() == password.toString()) {
                                    saveToSharedPrefrenceAndMoveToMainActivityEmp(mail.toString())
                                } else if (it.child("password").value.toString() == "") {
                                    Toast.makeText(
                                        this,
                                        "May your acccount registred with google please sign in with google",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(this, "please enter proper password", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }.addOnFailureListener {
                                Log.e("firebase", "Error getting data", it)
                                Toast.makeText(this, "Error getting data from firebase", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }.addOnFailureListener {
                        Log.e("firebase", "Error getting data", it)
                        Toast.makeText(this, "Error getting data from firebase", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                }
            binding.progLogIn.visibility = View.GONE

        }
        binding.googleSignIn.setOnClickListener {
            signInGoogle()
        }
    }


    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResults(task)
            }
        }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                updateUI(account)
            }
        } else {
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val email = checkPass.mailDotNotTakingProblemSolved(account.email.toString())
                database.child("EmailToPhone").child(email).get().addOnSuccessListener {
                    Log.e("firebase", "entered in the on success")
                    if (it.value == null) {
                        Toast.makeText(
                            this,
                            "this Email is not registered",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        phoneAfterGmailSignIn = it.value.toString()
                        saveToSharedPrefrenceAndMoveToMainActivity(phoneAfterGmailSignIn)
                    }
                }.addOnFailureListener {
                    Log.e("firebase", "Error getting data", it)
                    Toast.makeText(this, "Error getting data from firebase", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

            }
        }
    }

//    private fun CheckingAcountExistence(NameOrEmail: String): Boolean {
//            database.child("users").child(NameOrEmail).get().addOnSuccessListener() {
//                if (it.value == null) {
//                    Toast.makeText(
//                        this,
//                        "this phone number is not registered",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    reasult = false
//                }
//            }.addOnFailureListener {
//                reasult = false
//                Log.e("firebase", "Error getting data", it)
//                Toast.makeText(this, "Error getting data from firebase", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        return reasult
//    }

    private fun saveToSharedPrefrenceAndMoveToMainActivity(phone: String) {
        editer.apply() {
            putBoolean("loggedin", true)
            putBoolean("custOrEmp", true)
            putBoolean("fst", true)
            putString("phone", phone.toString())
            apply()
        }
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("phone", phone.toString())
        intent.putExtra("commingFromLogin",true)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent)
        finish()
    }
    private fun saveToSharedPrefrenceAndMoveToMainActivityEmp(email: String) {
        editer.apply() {
            putBoolean("loggedin", true)
            putBoolean("custOrEmp", false)
            putBoolean("fst", true)
            putString("email", email.toString())
            apply()
        }
        val intent = Intent(this, MainActivityEmp::class.java)
        intent.putExtra("email", email.toString())
        intent.putExtra("commingFromLogin",true)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent)
        finish()
    }

    //    private fun passwordFocusListener()
//    {
//        binding.logpass.setOnFocusChangeListener { _, focused ->
//            if(!focused)
//            {
//                binding.logPassHelper.helperText = checkPass.validPassword(binding.logpass.text.toString())
//            }
//        }
//    }
//    private fun PhoneFocusListener()
//    {
//        binding.logphone.setOnFocusChangeListener { _, focused ->
//            if(!focused)
//            {
//                if(checkPass.isValidMobile(binding.logphone.text.toString()))binding.regPhoneHelper.helperText = null
//                else binding.regPhoneHelper.helperText = "Please enter a valid phone number"
//            }
//        }
//    }
//

}