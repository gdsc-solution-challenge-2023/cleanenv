package com.example.cleanenv.SplashScreenAndLogin

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.cleanenv.MainActivity
import com.example.cleanenv.R
import com.example.cleanenv.Utils.Registrationeed
import com.example.cleanenv.databinding.ActivityRegisterBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

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
    private lateinit var googleSignInClient : GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
//        NameFocusListener()
//        PhoneFocusListener()
//        EmailFocusListener()
//        passwordFocusListener()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this , gso)
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
            if (name == "" || phone == "" || email == "" || password == "" || conPass.toString() == "") {
                Toast.makeText(this, "please enter all the details", Toast.LENGTH_SHORT).show()
            }
            else {
                database.child("users").child(phone.toString()).get().addOnSuccessListener() {
                    if (it.value != null) {
                        Toast.makeText(
                            this,
                            "this user id is already registered",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        if (allOk()) {
                            val User = user(name, phone, email, password)
                            phone?.let {
                                database.child("users").child(it).setValue(User)
                                    .addOnSuccessListener {
                                        binding.regName.text?.clear()
                                        binding.regPhone.text?.clear()
                                        binding.regPass.text?.clear()
                                        Toast.makeText(
                                            this,
                                            "successfully Registered",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }.addOnFailureListener {
                                    Toast.makeText(
                                        this,
                                        "firebase not connected",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } else Toast.makeText(
                            this,
                            "please enter all the details properly",
                            Toast.LENGTH_SHORT
                        ).show();
                    }
                }.addOnFailureListener {
                    Log.e("firebase", "Error getting data", it)
                    Toast.makeText(this, "Error getting data from firebase", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            binding.prog.visibility = View.GONE

        }


        binding.googleSignUp.setOnClickListener{
//            signInGoogle()
            showEditTextDialog()
        }

        binding.btnLogRegister.setOnClickListener {
            onBackPressed()
        }
    }

    private fun showEditTextDialog() {
        val builder = Dialog(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.edit_text_layout,null)

        builder.setContentView(dialogLayout)
        builder.setCancelable(true)
        val text = dialogLayout.findViewById<EditText>(R.id.txt_input)
        val button = dialogLayout.findViewById<Button>(R.id.btn_okay)
        builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        builder.show()
        button.setOnClickListener{
            showOtp(text.text.toString())
            Toast.makeText(this@Register, "${text.text.toString()}", Toast.LENGTH_SHORT).show()
            builder.dismiss()
        }
    }

    private fun showOtp(toString: String) {
        val builder = Dialog(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.edit_text_layout,null)

        builder.setContentView(dialogLayout)
        builder.setCancelable(true)
        val text = dialogLayout.findViewById<EditText>(R.id.txt_input)
        val button = dialogLayout.findViewById<Button>(R.id.btn_okay)
        text.hint = "Enter the Otp"
//        dialogLayout.findViewById<Button>(R.id.button).setOnClickListener{
//            dialogLayout.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.chekceee)
//        }
        builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        builder.show()
        button.setOnClickListener{
            Toast.makeText(this@Register, "${toString} is registred", Toast.LENGTH_SHORT).show()
            builder.dismiss()
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

//    private fun passwordFocusListener()
//    {
//        binding.regPass.setOnFocusChangeListener { _, focused ->
//            if(!focused)
//            {
//                binding.regPassHelper.helperText = checkPass.validPassword(binding.regPass.text.toString())
//            }
//        }
//    }

    private fun signInGoogle(){
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if (result.resultCode == Activity.RESULT_OK){

            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful){
            val account : GoogleSignInAccount? = task.result
            if (account != null){
                updateUI(account)
            }
        }else{
            Toast.makeText(this, task.exception.toString() , Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken , null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful){
                val intent : Intent = Intent(this , MainActivity::class.java)
                intent.putExtra("email" , account.email)
                intent.putExtra("name" , account.displayName)
                intent.putExtra("pic",account.photoUrl.toString())
                startActivity(intent)
//                finish()
            }else{
                Toast.makeText(this, it.exception.toString() , Toast.LENGTH_SHORT).show()

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
