package com.example.duestake.data.emptra

data class PinCodeAddress(
    val code: Int,
    val result: PinCodeResult
)

data class PinCodeResult(
    val `data`: List<PinCodeData>,
    val request_id: String
)

data class PinCodeData(
    val area_name: String,
    val city_name: String,
    val district_name: String,
    val division_name: String,
    val office_name: String,
    val pincode: String,
    val region_name: String,
    val state_name: String
)