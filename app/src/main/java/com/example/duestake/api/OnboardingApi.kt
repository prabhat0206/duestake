package com.example.duestake.api

import com.example.duestake.data.auth.BaseResponse
import com.example.duestake.data.onboarding.*
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface OnboardingApi {

    @POST("userdetails/updateUserByIdLevel1/{id}")
    suspend fun sendLoanDetails(
        @Path("id") id: String,
        @Body loanDetails: LoanDetails
    ): BaseResponse

    @POST("userdetails/updateUserByIdLevel2/{id}")
    suspend fun sendPersonalDetails(
        @Path("id") id: String,
        @Body personalDetails: PersonalDetails
    ): BaseResponse

    @POST("userdetails/updateUserByIdLevel3/{id}")
    suspend fun sendAddressVerification(
        @Path("id") id: String,
        @Body addressVerification: AddressVerification
    ): BaseResponse

    @POST("userdetails/updateUserByIdLevel4/{id}")
    suspend fun sendKYCDetails(
        @Path("id") id: String,
        @Body kycDetails: KYCDetails
    ): BaseResponse

    @POST("userdetails/updateUserByIdLevel5/{id}")
    suspend fun sendEmploymentDetails(
        @Path("id") id: String,
        @Body employmentDetails: EmploymentDetails
    ): BaseResponse

    @POST("userdetails/updateUserByIdByPanDetails/{id}")
    suspend fun updatePanDetails(
        @Path("id") id: String,
        @Body updatePanDetails: UpdatePanDetails
    ): BaseResponse
}