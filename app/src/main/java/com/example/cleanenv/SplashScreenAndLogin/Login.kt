package com.example.cleanenv.SplashScreenAndLogin

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.cleanenv.MainActivity
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
    val checkPass = Registrationeed()
    private lateinit var database: DatabaseReference
    private lateinit var auth : FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://verdant-volt-default-rtdb.firebaseio.com/")

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this , gso)

        binding.btnRegLogin.setOnClickListener {
            startActivity(Intent(this,Register::class.java))
            overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left)
        }
        passwordFocusListener()
        PhoneFocusListener()

        binding.logbutton.setOnClickListener {
//            signInGoogle()
            var phone = binding.logphone.text
            var password = binding.logpass.text
            database.child("users").child(phone.toString()).get().addOnSuccessListener(){
//                Log.i("firebase", "Got value ${it.child("password").value}")
//                Toast.makeText(this, "${it.child("password").value}", Toast.LENGTH_SHORT).show()
                if(it.value==null){
                    Toast.makeText(this, "this user id is not registered", Toast.LENGTH_SHORT).show()
                }
                else if(it.child("password").value==password.toString()){
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("email" , it.child("email").value.toString())
                    intent.putExtra("name" , it.child("name").value.toString())
//                    intent.putExtra("pic",it.child("password").value)
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(this, "please enter proper password", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }

        }
    }

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
    private fun passwordFocusListener()
    {
        binding.logpass.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.logPassHelper.helperText = checkPass.validPassword(binding.logpass.text.toString())
            }
        }
    }
    private fun PhoneFocusListener()
    {
        binding.logphone.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                if(checkPass.isValidMobile(binding.logphone.text.toString()))binding.regPhoneHelper.helperText = null
                else binding.regPhoneHelper.helperText = "Please enter a valid phone number"
            }
        }
    }
}