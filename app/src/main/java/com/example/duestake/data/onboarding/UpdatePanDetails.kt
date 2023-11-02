package com.example.duestake.data.onboarding

data class UpdatePanDetails(
    val userAdharCardNo: Long,
    val userCity: String,
    val userDOB: String,
    val userFullName: String,
    val userGender: String,
    val userPANNumber: String,
    val userPincode: Int,
    val userState: String
)