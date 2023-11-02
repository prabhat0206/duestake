package com.example.duestake.data

import androidx.lifecycle.LiveData
import com.example.duestake.api.AuthApi
import com.example.duestake.api.EmptraApi
import com.example.duestake.api.HomeApi
import com.example.duestake.api.OnboardingApi
import com.example.duestake.dao.UserDao
import com.example.duestake.data.auth.*
import com.example.duestake.data.bank.Bank
import com.example.duestake.data.emptra.*
import com.example.duestake.data.onboarding.*
import com.example.duestake.data.user.UserData
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Singleton

class NetworkException(message: String, cause: Throwable) : Exception(message, cause)

@Singleton
class MainRepository @Inject constructor(
    private val authApi: AuthApi,
    private val onboardingApi: OnboardingApi,
    private val emptraApi: EmptraApi,
    private val homeApi: HomeApi,
    private val userDao: UserDao
) {

    private suspend fun <T : Any> safeApiCall(apiCall: suspend () -> T): Result<T> {
        return try {
            val response = apiCall.invoke()
            Result.Success(response)
        } catch (e: SocketTimeoutException) {
            Result.Error(NetworkException("Network timeout", e))
        } catch (e: ConnectException) {
            Result.Error(NetworkException("Network connection failed", e))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun sendRegisterOtp(otp: Otp): Result<BaseResponse> {
        return safeApiCall { authApi.sendRegisterOtp(otp) }
    }

    suspend fun registerUser(registerUser: RegisterUser): Result<UserInformation> {
        return safeApiCall { authApi.registerUser(registerUser) }
    }

    suspend fun sendLoginOtp(otp: Otp): Result<BaseResponse> {
        return safeApiCall { authApi.sendLoginOtp(otp) }
    }

    suspend fun loginUser(loginUser: LoginUser): Result<UserInformation> {
        return safeApiCall { authApi.verifyLoginOtp(loginUser) }
    }

    suspend fun sendLoanDetails(userId: String, loanDetails: LoanDetails): Result<BaseResponse> {
        return safeApiCall { onboardingApi.sendLoanDetails(userId, loanDetails) }
    }

    suspend fun sendPersonalDetails(
        userId: String,
        personalDetails: PersonalDetails
    ): Result<BaseResponse> {
        return safeApiCall { onboardingApi.sendPersonalDetails(userId, personalDetails) }
    }

    suspend fun sendAddressVerification(
        userId: String,
        addressVerification: AddressVerification
    ): Result<BaseResponse> {
        return safeApiCall { onboardingApi.sendAddressVerification(userId, addressVerification) }
    }

    suspend fun sendKYCDetails(userId: String, kycDetails: KYCDetails): Result<BaseResponse> {
        return safeApiCall { onboardingApi.sendKYCDetails(userId, kycDetails) }
    }

    suspend fun sendEmploymentDetails(
        userId: String,
        employmentDetails: EmploymentDetails
    ): Result<BaseResponse> {
        return safeApiCall { onboardingApi.sendEmploymentDetails(userId, employmentDetails) }
    }

    suspend fun updatePanDetails(
        userId: String,
        updatePanDetails: UpdatePanDetails
    ): Result<BaseResponse> {
        return safeApiCall { onboardingApi.updatePanDetails(userId, updatePanDetails) }
    }

    suspend fun getBankList(): Result<Bank> {
       return safeApiCall { homeApi.bankList() }
    }

//    suspend fun addressByPinCode(pinCode: PinCode) = emptraApi.addressByPinCode(pinCode)

    suspend fun panDetails(panNumber: PanNumber): Result<PanDetails> {
        return safeApiCall { emptraApi.panDetails(panNumber) }
    }

//    suspend fun bankIfsc(ifscCode: IfscCode) = emptraApi.bankIfsc(ifscCode)

    suspend fun companyList(companyName: CompanyName) : Result<CompanyList> {
        return safeApiCall { emptraApi.companyList(companyName) }
    }

    val allEntities: LiveData<UserData> = userDao.getAll()

    suspend fun insert(userData: UserData) {
        userDao.insertAll(userData)
    }
}