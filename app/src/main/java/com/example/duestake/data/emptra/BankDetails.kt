package com.example.duestake.data.emptra

data class BankDetails(
    val code: Int,
    val result: BankDetailsResult
)

data class BankDetailsResult(
    val address: String,
    val branch: String,
    val city: String,
    val code: String,
    val contact: String,
    val district: String,
    val ifsc: String,
    val imps: Boolean,
    val micr: String,
    val name: String,
    val neft: Boolean,
    val request_id: String,
    val rtgs: Boolean,
    val state: String,
    val upi: Boolean
)