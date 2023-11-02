package com.example.duestake.data.onboarding

data class AddressVerification(
    val userCity: String,
    val userPincode: Int,
    val userResidentType: String,
    val userState: String
)