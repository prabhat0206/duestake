package com.example.duestake.data.emptra

data class CompanyList(
    val code: Int,
    val message: Message
)
data class Message(
    val `data`: List<CompanyData>
)

data class CompanyData(
    val dataid: String,
    val name: String
)
