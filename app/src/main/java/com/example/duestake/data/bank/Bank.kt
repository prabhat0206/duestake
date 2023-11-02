package com.example.duestake.data.bank

data class Bank(
    val banks: List<BankList>,
    val success: Boolean
)

data class BankList(
    val BankLogo: String,
    val Description: String,
    val Title: String,
    val _id: String,
    val updatedAt: String
)