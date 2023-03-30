package com.example.cleanenv.Utils

import android.util.Patterns
import androidx.core.text.trimmedLength

class Registrationeed {
    fun validPassword(passwordText: String): String?
    {
        if(passwordText.length < 8)
        {
            return "Minimum 8 Character Password"
        }
        if(!passwordText.matches(".*[A-Z].*".toRegex()))
        {
            return "Must Contain 1 Upper-case Character"
        }
        if(!passwordText.matches(".*[a-z].*".toRegex()))
        {
            return "Must Contain 1 Lower-case Character"
        }
        if(!passwordText.matches(".*[@#\$%^&+=].*".toRegex()))
        {
            return "Must Contain 1 Special Character (@#\$%^&+=)"
        }

        return null
    }
    fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    fun isValidMobile(phone: String): Boolean {
        return phone.trimmedLength() in (10..13) && Patterns.PHONE.matcher(phone).matches()
    }

    fun mailDotNotTakingProblemSolved(email: String): String {
        var ans = ""
        for (i in 0..email.length-1){
            if(email[i]!='.')ans+=email[i]
        }
        return ans
    }
    fun isValidMobileAgainstIndian(phone: String?) : String? {
        var phonee = phone
        if (phone != null) {
            if(phone.get(0).toString()!="+"){
                phonee = "+91${phone}";
            }
        }
        return phonee
    }


}