package com.example.cleanenv.SplashScreenAndLogin

import com.example.cleanenv.Utils.bank

data class user(
    val name: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val Password: String? = null,
    val pic: String? = null,
    val address: String? = null,
    val bank: bank
)