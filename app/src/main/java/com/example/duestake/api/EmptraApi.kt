package com.example.duestake.api

import com.example.duestake.data.Constant
import com.example.duestake.data.emptra.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface EmptraApi {

    @POST("emptra/pincodeInformation")
    suspend fun addressByPinCode(
        @Body pinCode: PinCode
    ): PinCodeAddress

    @POST("panCard/V2")
    suspend fun panDetails(
        @Body panNumber: PanNumber
    ): PanDetails

    @POST("emptra/ifscCodeInformation")
    suspend fun bankIfsc(
        @Body ifscCode: IfscCode
    ): BankDetails

    @POST("companySearch")
    suspend fun companyList(
        @Body companyName: CompanyName
    ): CompanyList
}