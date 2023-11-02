package com.example.duestake.ui.fragments

import android.app.Application
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.duestake.R
import com.example.duestake.data.InternetConnectionLiveData

open class BaseFragment: Fragment() {

    protected var isInternetConnected = false

    fun hideProgressBar(view: View) {
        view.visibility = View.GONE
    }

    fun showProgressBar(view: View) {
        view.visibility = View.VISIBLE
    }

    fun hideErrorMessage(view: View) {
        view.visibility = View.GONE
    }

    fun showErrorMessage(message: String, errorView: View, errorMessageView: TextView) {
        errorView.visibility = View.VISIBLE
        errorMessageView.text = message
    }

    fun markButtonDisable(button: Button) {
        button.isEnabled = false
        button.setTextColor(ContextCompat.getColor(button.context, R.color.hint))
    }

    fun markButtonEnable(button: Button) {
        button.isEnabled = true
        button.setTextColor(ContextCompat.getColor(button.context, R.color.white))
    }

    fun checkNetworkConnection(application: Application) {
        val internetConnectionLiveData = InternetConnectionLiveData(application)

        internetConnectionLiveData.observe(viewLifecycleOwner ) { isConnected ->
            isInternetConnected = isConnected
        }
    }

    val loanType = mutableListOf(
        "Personal Loan",
        "Business Loan",
        "Travel Loan",
        "Marriage Loan",
        "Car Loan",
        "Two-Wheeler Loan",
        "Medical Loan",
        "Education Loan",
        "Home Loan",
        "Gold Loan"
    )

    val gender = mutableListOf("Male", "Female", "Other")

    val maritalStatus = mutableListOf("Single", "Married", "Divorce")

    val qualification = mutableListOf("Undergraduate", "Graduate", "Postgraduate", "Other")

    val residentType = mutableListOf("Self owned", "Owned by Parents", "Owned by siblings", "Rental")

    val employmentType = mutableListOf("Salaried", "Self-Owned", "Other")

    val companyType = mutableListOf("Private", "Public", "Government", "Other")

    val modeOfSalary = mutableListOf("Cash", "Bank Deposit")

}