package com.example.duestake.api

import com.example.duestake.data.auth.*
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/sendOtp")
    suspend fun sendRegisterOtp(
        @Body otp: Otp
    ): BaseResponse

    @POST("auth/userRegister")
    suspend fun registerUser(
        @Body registerUser: RegisterUser
    ): UserInformation

    @POST("auth/userLogin")
    suspend fun sendLoginOtp(
        @Body otp: Otp
    ): BaseResponse

    @POST("auth/verifyOtp")
    suspend fun verifyLoginOtp(
        @Body loginUser: LoginUser
    ): UserInformation
}