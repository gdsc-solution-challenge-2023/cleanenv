package com.example.cleanenv.Utils

import java.util.regex.Pattern

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
        return android.util.Patterns.PHONE.matcher(phone).matches()
    }

}