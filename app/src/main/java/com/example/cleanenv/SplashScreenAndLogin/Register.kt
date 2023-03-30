package com.example.cleanenv.SplashScreenAndLogin

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.example.cleanenv.Utils.bank
import com.example.cleanenv.databinding.ActivityRegisterBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.TimeUnit

class Register : AppCompatActivity() {
    val prefname = "myPref"
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editer : SharedPreferences.Editor
    private lateinit var database: DatabaseReference
    private lateinit var auth : FirebaseAuth
    lateinit var binding: ActivityRegisterBinding
    private var forceResendingToken : PhoneAuthProvider.ForceResendingToken? = null
    private var Callbacks : PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null
    private var mVerifiactionId : String? = null
    val checkPass = Registrationeed()
    var phone :String? = null
    var name: String? =null
    var email:String = ""
    var Pic:String = ""
    var password:String?=null
    var conPass:String?=null
    var errorMsg = ""
    var showEditTextDialogIfShowedOrNot = false
    lateinit var builder : Dialog
    private lateinit var googleSignInClient : GoogleSignInClient
    private lateinit var progrsDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initt()
//        NameFocusListener()
//        PhoneFocusListener()
//        EmailFocusListener()
//        passwordFocusListener()

//
        binding.RegisterButton.setOnClickListener{
            name = binding.regName.text.toString()
            phone = binding.regPhone.text.toString()
            email = binding.emailPass.text.toString()
            password = binding.regPass.text.toString()
//            confirmFocusListener()
            if (name == "" || phone == "" || email == "" || password == "" || conPass.toString() == "") {
                Toast.makeText(this, "please enter all the details", Toast.LENGTH_SHORT).show()
            }else if(!checkPass.isValidMobile(phone.toString())){
                Toast.makeText(this, "please enter a proper number", Toast.LENGTH_SHORT).show()
            }
            else {
                    if(phone!="")phone = checkPass.isValidMobileAgainstIndian(phone!!)
                    progrsDialog.setMessage("verifying")
                    progrsDialog.show()
                    startVerifying(phone!!)
            }

        }


        binding.googleSignUp.setOnClickListener{
            signInGoogle()
        }

        binding.btnLogRegister.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initt() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        progrsDialog = ProgressDialog(this@Register)
        googleSignInClient = GoogleSignIn.getClient(this , gso)
//
        sharedPreferences = this.getSharedPreferences(prefname, Context.MODE_PRIVATE)
        editer = sharedPreferences.edit()
        auth = FirebaseAuth.getInstance()
//        auth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);
        database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://cleanenv-4ca72-default-rtdb.firebaseio.com/")

        Callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                singInWithPhoneAuthCredentioal(phoneAuthCredential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                errorMsg = e.message.toString()
                Toast.makeText(this@Register, errorMsg, Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(VerificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                mVerifiactionId = VerificationId
                forceResendingToken = token

                Toast.makeText(this@Register, "Code sent", Toast.LENGTH_SHORT).show()
                progrsDialog.dismiss()
                showOtp(name!!, phone!!, password!!)

            }

        }
    }

    private fun showEditTextDialog(namee: String, emaill: String, passwordd: String) {
        builder = Dialog(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.edit_text_layout,null)

        builder.setContentView(dialogLayout)
        builder.setCancelable(true)
        val text = dialogLayout.findViewById<EditText>(R.id.txt_input)
        val button = dialogLayout.findViewById<Button>(R.id.btn_okay)
        builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        builder.show()

        showEditTextDialogIfShowedOrNot = true
        button.setOnClickListener{
            progrsDialog.setMessage("verifying")
            progrsDialog.show()
            phone = text.text.toString()
            name = namee
            email = emaill
            password = passwordd
            if(!checkPass.isValidMobile(phone!!)){
                Toast.makeText(this, "please enter a proper number", Toast.LENGTH_SHORT).show()
            }
            else {
                if(phone!="")phone = checkPass.isValidMobileAgainstIndian(phone!!)
                startVerifying(phone!!)
            }
        }
    }
    private fun showOtp(name: String, phone: String, password: String) {
        if(showEditTextDialogIfShowedOrNot){
            builder.dismiss()
        }
        builder = Dialog(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.edit_text_layout,null)
        builder.setContentView(dialogLayout)
        builder.setCancelable(true)
        val text = dialogLayout.findViewById<EditText>(R.id.txt_input)
        val reSendbutton = dialogLayout.findViewById<Button>(R.id.btn_resend)
        val cancel = dialogLayout.findViewById<Button>(R.id.btn_cancel)
//        val prog = dialogLayout.findViewById<ProgressBar>(R.id.progressBar)
        reSendbutton.visibility = View.VISIBLE
        val button = dialogLayout.findViewById<Button>(R.id.btn_okay)
        text.hint = "Enter the Otp"
        button.text = "Submit"
        builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        builder.show()
        if(errorMsg!="")builder.dismiss()
        button.setOnClickListener{
            progrsDialog.setMessage("verifying")
            progrsDialog.show()
            if(text.text.toString()!="" || text.text.toString().length!=6)verifyNumberWithCode(mVerifiactionId,text.text.toString())
            else Toast.makeText(this@Register, "please enter proper OTP", Toast.LENGTH_SHORT).show()
//            prog.visibility = View.VISIBLE

//            Toast.makeText(this@Register, "${toString} is registred", Toast.LENGTH_SHORT).show()
        }
        reSendbutton.setOnClickListener {
            ResendVerifying(phone,forceResendingToken)
        }
        cancel.setOnClickListener {
            builder.dismiss()
        }
    }
//
//
//    /// phone verifying
    private fun startVerifying(phone: String) {

        val option = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(Callbacks as PhoneAuthProvider.OnVerificationStateChangedCallbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(option)
    }
    private fun ResendVerifying(phone: String,token : PhoneAuthProvider.ForceResendingToken?) {

        val option = token?.let {
            PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setForceResendingToken(it)
                .setCallbacks(Callbacks as PhoneAuthProvider.OnVerificationStateChangedCallbacks)
                .build()
        }

        if (option != null) {
            PhoneAuthProvider.verifyPhoneNumber(option)
        }

    }
//
    private fun verifyNumberWithCode(verificationId : String?, code : String) {
        val credential = verificationId?.let { PhoneAuthProvider.getCredential(it,code) }
        return singInWithPhoneAuthCredentioal(credential)
    }
//
    private fun singInWithPhoneAuthCredentioal(credential: PhoneAuthCredential?) {
        if (credential != null) {
            auth.signInWithCredential(credential)
                .addOnSuccessListener{
                    save(name,phone,email,password)
                }.addOnFailureListener{e->
                    progrsDialog.dismiss()
                    Toast.makeText(this@Register, "${e.message}", Toast.LENGTH_SHORT).show()

                }
        }
    }

    private fun save(name: String?, phone: String?, email: String?, password: String?) {
        database.child("users").child(phone.toString()).get().addOnSuccessListener() {
            if (it.value != null) {
                Toast.makeText(
                    this,
                    "This phone number is already registered",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                val bank = bank("","","")
                val User = user(name, phone, email, password,Pic,"",bank)
                phone?.let {
                    database.child("users").child(it).setValue(User)
                        .addOnSuccessListener {
                            binding.regName.text?.clear()
                            binding.regPhone.text?.clear()
                            binding.emailPass.text?.clear()
                            binding.regPass.text?.clear()
                            editer.putBoolean("loggedin", true)
                            editer.putString("phone", phone.toString())
                            editer.putString("email", email.toString())
                            editer.apply()
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("phone", phone.toString())
                            startActivity(intent)
                            finish()
                        }.addOnFailureListener {
                            Toast.makeText(
                                this,
                                "Network Problem",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }
            }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
            Toast.makeText(this, "Network Error", Toast.LENGTH_SHORT)
                .show()
        }
        builder.dismiss()
        progrsDialog.dismiss()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right)
    }
//
////    private fun confirmFocusListener() {
////        binding.regPassCon.setOnFocusChangeListener { _, focused ->
////            if(!focused)
////            {
////                if (binding.regPass.text.toString() != binding.regPassCon.text.toString()) {
////                    binding.regPassConfirmHelper.helperText = "please enter same password in both"
////                }else binding.regPassConfirmHelper.helperText = null
////            }
////        }
////    }
//
//
//    private fun allOk(): Boolean {
//        if(binding.regNameHelper.helperText ==null && binding.regPhoneHelper.helperText == null && binding.regPassHelper.helperText == null )return true
//        return false
//    }
//
////    private fun NameFocusListener()
////    {
////        binding.regName.setOnFocusChangeListener { _, focused ->
////            if(!focused)
////            {
////                if(binding.regName.text.toString()!="")binding.regNameHelper.helperText = null
////            }
////        }
////    }
////    private fun PhoneFocusListener()
////    {
////        binding.regPhone.setOnFocusChangeListener { _, focused ->
////            if(!focused)
////            {
////                if(checkPass.isValidMobile(binding.regPhone.text.toString()))binding.regPhoneHelper.helperText = null
////                else binding.regPhoneHelper.helperText = "Please enter a valid phone number"
////            }
////        }
////    }
////    private fun EmailFocusListener()
////    {
////        binding.regEmail.setOnFocusChangeListener { _, focused ->
////            if(!focused)
////            {
////                if(checkPass.isEmailValid(binding.regEmail.text.toString()))binding.regEmailHelper.helperText = null
////                else binding.regEmailHelper.helperText = "Please enter a valid Email id"
////            }
////        }
////    }
//
////    private fun passwordFocusListener()
////    {
////        binding.regPass.setOnFocusChangeListener { _, focused ->
////            if(!focused)
////            {
////                binding.regPassHelper.helperText = checkPass.validPassword(binding.regPass.text.toString())
////            }
////        }
////    }
//
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
//
    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken , null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful){
                email = checkPass.mailDotNotTakingProblemSolved(account.email.toString())
                database.child("EmailToPhone").child(email).get().addOnSuccessListener {
                    if (it.value != null) {
                        Toast.makeText(
                            this,
                            "this email is already registered",
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{
                        showEditTextDialog(account.displayName.toString(),account.email.toString(),"")
                        Pic = account.photoUrl.toString()
                    }
                }.addOnFailureListener {
                    Log.e("firebase", "Error getting data", it)
                    Toast.makeText(this, "Error getting data from firebase", Toast.LENGTH_SHORT)
                        .show()
                }
            }else{
                Toast.makeText(this, it.exception.toString() , Toast.LENGTH_SHORT).show()

            }
        }
    }

//    private fun checkingEmailExistance(email: String): Boolean {
//        var reasult = true
//        database.child("EmailToPhone").child(email).get().addOnSuccessListener {
//            if (it.value != null) {
//                Toast.makeText(
//                    this,
//                    "this email is already registered",
//                    Toast.LENGTH_SHORT
//                ).show()
//                reasult = false
//            }
//        }.addOnFailureListener {
//            reasult = false
//            Log.e("firebase", "Error getting data", it)
//            Toast.makeText(this, "Error getting data from firebase", Toast.LENGTH_SHORT)
//                .show()
//        }
//        return reasult
//    }


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
