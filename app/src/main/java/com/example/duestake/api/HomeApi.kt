package com.example.duestake.api

import com.example.duestake.data.auth.BaseResponse
import com.example.duestake.data.bank.Bank
import retrofit2.Response
import retrofit2.http.GET

interface HomeApi {

    @GET("bank/getAllBanks")
    suspend fun bankList(): Bank
}