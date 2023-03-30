package com.example.cleanenv

data class fullOrder(
    val order: ArrayList<String>? = null,
    val money: Int? = null,
    val name: String? =null,
    val orderBy: String? =null,
    val emp: String?=null,
    val done: Boolean?=false,
    val address: String?=null)
