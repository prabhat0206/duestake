package com.example.duestake.data.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserData(
    @PrimaryKey
    val _id: String,
    val userAdharCardNo: Long? = null,
    val userBankIFSCCode: String? = "",
    val userBankName: String? = "",
    val userCity: String? = "",
    val userCompanyCity: String? = "",
    val userCompanyIndustry: String? = "",
    val userCompanyName: String? = null,
    val userCompanyPincode: Int? = null,
    val userCompanyState: String? = null,
    val userCompanyType: String? = null,
    val userContactNo: String? = null,
    val userDOB: String? = null,
    val userDesignation: String? = null,
    val userEmail: String? = null,
    val userExperienceInCompany: String? = null,
    val userFatherName: String? = null,
    val userFullName: String? = null,
    val userGender: String? = null,
    val userLoanAmount: Int? = null,
    val userLoanTenure: Int? = null,
    val userLoanType: String? = null,
    val userMaritalStatus: String? = null,
    val userModeOfSalary: String? = null,
    val userMonthlyExpense: Int? = 0,
    val userMonthlyIncome: Int? = null,
    val userMotherName: String? = null,
    val userPANNumber: String? = null,
    val userPincode: Int? = null,
    val userProfilePhoto: String? = null,
    val userQualification: String? = null,
    val userResidentType: String? = null,
    val userSalariedStatus: String? = null,
    val userState: String? = null
)