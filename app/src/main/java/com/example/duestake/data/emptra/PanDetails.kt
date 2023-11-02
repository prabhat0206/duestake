package com.example.duestake.data.emptra

data class PanDetails(
    val code: Int,
    val result: PanResult
)

data class PanResult(
    val `data`: PanData
)

data class PanData(
    val AADHAR_NUM: Long,
    val ADDRESS_1: String,
    val ADDRESS_2: String,
    val ADDRESS_3: String,
    val CITY: String,
    val COUNTRY: String,
    val DOB: String,
    val EMAIL: String,
    val FIRST_NAME: String,
    val GENDER: String,
    val IDENTITY_TYPE: String,
    val LAST_NAME: String,
    val MIDDLE_NAME: String,
    val MOBILE_NO: String,
    val PAN: String,
    val PINCODE: String,
    val STATE: String
)