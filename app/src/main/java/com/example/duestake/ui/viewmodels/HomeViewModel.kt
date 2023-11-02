package com.example.duestake.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duestake.data.MainRepository
import com.example.duestake.data.Result
import com.example.duestake.data.bank.Bank
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel(){

    val bankListResource = MutableLiveData<Result<Bank>>()

    fun bankList() = viewModelScope.launch {
        bankListResource.postValue(Result.Loading())
        mainRepository.getBankList().let {
            bankListResource.postValue(it)
        }
    }

}