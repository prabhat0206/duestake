package com.example.duestake.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duestake.data.MainRepository
import com.example.duestake.data.Result
import com.example.duestake.data.auth.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    val registerOtpResource = MutableLiveData<Result<BaseResponse>>()

    val loginOtpResource = MutableLiveData<Result<BaseResponse>>()

    val registerUserResource = MutableLiveData<Result<UserInformation>>()

    val loginUserResource = MutableLiveData<Result<UserInformation>>()



    fun registerUser(registerUser: RegisterUser) = viewModelScope.launch {
        registerUserResource.postValue(Result.Loading())
        mainRepository.registerUser(registerUser).let {
            registerUserResource.postValue(it)
        }
    }

    fun loginUser(loginUser: LoginUser) = viewModelScope.launch {
        loginUserResource.postValue(Result.Loading())
        mainRepository.loginUser(loginUser).let {
            loginUserResource.postValue(it)
        }
    }

}