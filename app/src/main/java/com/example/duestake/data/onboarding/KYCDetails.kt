package com.example.duestake.data.onboarding

data class KYCDetails(
    val userAdharCardNo: Long,
    val userBankIFSCCode: String,
    val userBankName: String,
    val userPANNumber: String
)