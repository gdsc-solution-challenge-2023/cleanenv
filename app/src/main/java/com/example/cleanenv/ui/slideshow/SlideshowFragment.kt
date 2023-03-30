package com.example.cleanenv.ui.slideshow

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cleanenv.profile
import com.example.cleanenv.R
import com.example.cleanenv.Utils.Registrationeed
import com.example.cleanenv.Utils.bank
import com.example.cleanenv.databinding.FragmentSlideshowBinding
import com.example.cleanenv.ui.home.newAddress
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.TimeUnit

class SlideshowFragment : Fragment() {
    val prefname = "myPref"
    private var _binding: FragmentSlideshowBinding? = null
    lateinit var sharedPreferences: SharedPreferences
    private var forceResendingToken : PhoneAuthProvider.ForceResendingToken? = null
    private var mVerifiactionId : String? = null
    val bundle = arguments
    private var phone = ""
    private var phoneold = ""
    var errorMsg = ""
    private var Callbacks : PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null
    private lateinit var auth : FirebaseAuth
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val checkPass = Registrationeed()
    lateinit var builder : Dialog
    private lateinit var database: DatabaseReference
    private lateinit var progrsDialog: ProgressDialog
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(SlideshowViewModel::class.java)

        initt()

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://cleanenv-4ca72-default-rtdb.firebaseio.com/").child("users")
        binding.phoneConst.setOnClickListener {
            showEditTextDialog()
        }

        binding.addressConst.setOnClickListener {
            val intent = Intent(requireContext(),newAddress::class.java)
            startActivity(intent)
        }

        binding.profileConst.setOnClickListener {
            val intent = Intent(requireContext(),profile::class.java)
            startActivity(intent)
        }
        binding.BankConst.setOnClickListener {
            builder = Dialog(requireContext())
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.bank_details_update,null)
            builder.setContentView(dialogLayout)
            builder.setCancelable(true)
            val name = dialogLayout.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etUserName)
            val actno = dialogLayout.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etBankAcntNo)
            val IFSCMp = dialogLayout.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etIFSCNo)
            val saveButtn = dialogLayout.findViewById<Button>(R.id.btSubmitBankDetails)
            builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            builder.show()
            saveButtn.visibility = View.VISIBLE
            saveButtn.setOnClickListener {
                val bankDetails = bank(name.text.toString(), actno.text.toString(),IFSCMp.text.toString())
                database.child(phoneold).child("bankDetails").setValue(bankDetails).addOnSuccessListener{
                    Toast.makeText(requireContext(), "saved", Toast.LENGTH_SHORT).show()
                    builder.dismiss()
                }.addOnFailureListener {
                    Toast.makeText(requireContext(), "firebase failed", Toast.LENGTH_SHORT).show()
                }
            }

        }
//        val textView: Button = binding.PlacingOrder
//        textView.setOnClickListener{
//            editer.apply() {
//                putBoolean("loggedin", false)
//                putString("phone", "")
//                apply()
//            }
//            val intent = Intent(context,Login::class.java)
//            startActivity(intent)
//        }
//        slideshowViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    private fun initt() {
        database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://cleanenv-4ca72-default-rtdb.firebaseio.com/").child("users")

        sharedPreferences = this.requireActivity().getSharedPreferences(prefname, Context.MODE_PRIVATE)
        val editer = sharedPreferences.edit()
        phoneold = sharedPreferences.getString("phone", null).toString()
        progrsDialog = ProgressDialog(requireContext())
        auth = FirebaseAuth.getInstance()
        Callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
            singInWithPhoneAuthCredentioal(phoneAuthCredential)
        }


        override fun onVerificationFailed(e: FirebaseException) {
            errorMsg = e.message.toString()
            Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
        }

        override fun onCodeSent(VerificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            mVerifiactionId = VerificationId
            forceResendingToken = token

            Toast.makeText(requireContext(), "Code sent", Toast.LENGTH_SHORT).show()
            progrsDialog.dismiss()
            showOtp()
        }

    }
    }

    private fun showEditTextDialog() {
        builder = Dialog(requireContext())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.edit_text_layout,null)

        builder.setContentView(dialogLayout)
        builder.setCancelable(true)
        val text = dialogLayout.findViewById<EditText>(R.id.txt_input)
        val button = dialogLayout.findViewById<Button>(R.id.btn_okay)
        builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        builder.show()
        button.setOnClickListener{
//            Toast.makeText(this@Register,text.text.toString().get(0).toString(),Toast.LENGTH_LONG).show()
            phone = text.text.toString()
            if(!checkPass.isValidMobile(phone!!)){
                Toast.makeText(requireContext(), "please enter a proper number", Toast.LENGTH_SHORT).show()
            }
            else {
                if(phone!="")phone = checkPass.isValidMobileAgainstIndian(phone!!).toString()
                startVerifying(phone!!)
            }
            }
    }

    private fun startVerifying(phone: String) {
        progrsDialog.setMessage("verifying")
        progrsDialog.show()

        val option = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(Callbacks as PhoneAuthProvider.OnVerificationStateChangedCallbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(option)
    }

    private fun verifyNumberWithCode(verificationId : String?, code : String) {
        val credential = verificationId?.let { PhoneAuthProvider.getCredential(it,code) }
        return singInWithPhoneAuthCredentioal(credential)
    }
    //
    private fun singInWithPhoneAuthCredentioal(credential: PhoneAuthCredential?) {
        if (credential != null) {
            auth.signInWithCredential(credential)
                .addOnSuccessListener{
                    save(phone)
                    progrsDialog.dismiss()
                    builder.dismiss()
                }.addOnFailureListener{e->
                    progrsDialog.dismiss()
                    Toast.makeText(requireContext(), "${e.message}", Toast.LENGTH_SHORT).show()

                }
        }
    }

    private fun save(phone: String) {
        Toast.makeText(requireContext(), "${phone} saved", Toast.LENGTH_SHORT).show()
        database.child("users").child(phoneold).get().addOnSuccessListener() {
            Toast.makeText(requireContext(), "${it.child("name").value.toString()}", Toast.LENGTH_SHORT).show()
            val temp = it.child("name").value.toString()
            database.child("users").child(phone).setValue(it.value).addOnSuccessListener() {
                Toast.makeText(requireContext(), "${phone} updated", Toast.LENGTH_SHORT).show()
//                emailToPhoneSave(phone)
            }.addOnFailureListener {
                Log.e("firebase", "Error getting data", it)
                Toast.makeText(requireContext(), "Error getting data from firebase", Toast.LENGTH_SHORT)
                    .show()
            }
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
            Toast.makeText(requireContext(), "Error getting data from firebase", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun emailToPhoneSave(phone: String) {
        database.child("EmailToPhone").child(phoneold).get()
            .addOnSuccessListener {
                database.child("EmailToPhone").child(phone).setValue(it.value)
                    .addOnSuccessListener {
                        Toast.makeText(
                            requireContext(),
                            "successfully Registered",
                            Toast.LENGTH_SHORT
                        ).show()
                    }.addOnFailureListener {
                        Toast.makeText(
                            requireContext(),
                            "Network Problem",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }.addOnFailureListener {
                Toast.makeText(
                    requireContext(),
                    "Network Problem",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun showOtp(){
        builder.dismiss()
        builder = Dialog(requireContext())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.edit_text_layout,null)
        builder.setContentView(dialogLayout)
        builder.setCancelable(false)
        val text = dialogLayout.findViewById<EditText>(R.id.txt_input)
        val reSendbutton = dialogLayout.findViewById<Button>(R.id.btn_resend)
        val cancel = dialogLayout.findViewById<Button>(R.id.btn_cancel)
        text.filters = arrayOf(InputFilter.LengthFilter(6))
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
            else Toast.makeText(requireContext(), "please enter proper OTP", Toast.LENGTH_SHORT).show()
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
    private fun ResendVerifying(phone: String,token : PhoneAuthProvider.ForceResendingToken?) {

        val option = token?.let {
            PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(requireActivity())
                .setForceResendingToken(it)
                .setCallbacks(Callbacks as PhoneAuthProvider.OnVerificationStateChangedCallbacks)
                .build()
        }

        if (option != null) {
            PhoneAuthProvider.verifyPhoneNumber(option)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}