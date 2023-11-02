package com.example.duestake.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duestake.data.MainRepository
import com.example.duestake.data.Result
import com.example.duestake.data.auth.BaseResponse
import com.example.duestake.data.emptra.*
import com.example.duestake.data.onboarding.*
import com.example.duestake.data.user.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {

    val loanDetailsResource = MutableLiveData<Result<BaseResponse>>()

    val personalDetailsResource = MutableLiveData<Result<BaseResponse>>()

    val addressVerificationResult = MutableLiveData<Result<BaseResponse>>()

    val kycDetailsResource = MutableLiveData<Result<BaseResponse>>()

    val employmentDetailsResource = MutableLiveData<Result<BaseResponse>>()

    val updatePanDetailsResource = MutableLiveData<Result<BaseResponse>>()

//    val pincodeAddressResource = MutableLiveData<Result<PinCodeAddress>>()

    val panDetailsResource = MutableLiveData<Result<PanDetails>>()

    val bankDetailsResource = MutableLiveData<Result<BankDetails>>()

    val companyListResource = MutableLiveData<Result<CompanyList>>()

    val companyName = MutableLiveData<String>()

    val userData :LiveData<UserData> = mainRepository.allEntities

    fun sendLoanDetails(userId: String, loanDetails: LoanDetails) = viewModelScope.launch {
        loanDetailsResource.postValue(Result.Loading())
        mainRepository.sendLoanDetails(userId, loanDetails).let {
            loanDetailsResource.postValue(it)
        }
    }

    fun sendPersonalDetails(userId: String, personalDetails: PersonalDetails) = viewModelScope.launch {
        personalDetailsResource.postValue(Result.Loading())
        mainRepository.sendPersonalDetails(userId, personalDetails).let {
            personalDetailsResource.postValue(it)
        }
    }

    fun sendAddressVerification(userId: String, addressVerification: AddressVerification) = viewModelScope.launch {
        addressVerificationResult.postValue(Result.Loading())
        mainRepository.sendAddressVerification(userId, addressVerification).let {
            addressVerificationResult.postValue(it)
        }
    }

    fun sendKYCDetails(userId: String, kycDetails: KYCDetails) = viewModelScope.launch {
        kycDetailsResource.postValue(Result.Loading())
        mainRepository.sendKYCDetails(userId, kycDetails).let {
            kycDetailsResource.postValue(it)
        }
    }

    fun sendEmploymentDetails(userId: String, employmentDetails: EmploymentDetails) = viewModelScope.launch {
        employmentDetailsResource.postValue(Result.Loading())
        mainRepository.sendEmploymentDetails(userId, employmentDetails).let {
            employmentDetailsResource.postValue(it)
        }
    }

    fun updatePanDetails(userId: String, updatePanDetails: UpdatePanDetails) = viewModelScope.launch {
        updatePanDetailsResource.postValue(Result.Loading())
        mainRepository.updatePanDetails(userId, updatePanDetails).let {
            updatePanDetailsResource.postValue(it)
        }
    }

//    fun addressByPinCode(pinCode: PinCode) = viewModelScope.launch {
//        pincodeAddressResource.postValue(Result.Loading())
//        mainRepository.addressByPinCode(pinCode).let {
//            pincodeAddressResource.postValue(handlePincodeAddressResource(it))
//        }
//    }

    fun panDetails(panNumber: PanNumber) = viewModelScope.launch {
        panDetailsResource.postValue(Result.Loading())
        mainRepository.panDetails(panNumber).let {
            panDetailsResource.postValue(it)
        }
    }

//    fun bankIfsc(ifscCode: IfscCode) = viewModelScope.launch {
//        bankDetailsResource.postValue(Result.Loading())
//        mainRepository.bankIfsc(ifscCode).let {
//            bankDetailsResource.postValue(handleBankDetailsResource(it))
//        }
//    }

    fun companyList(companyName: CompanyName) = viewModelScope.launch {
        companyListResource.postValue(Result.Loading())
        mainRepository.companyList(companyName).let {
            companyListResource.postValue(it)
        }
    }

    fun insert(userData: UserData) = viewModelScope.launch {
        mainRepository.insert(userData)
    }

}