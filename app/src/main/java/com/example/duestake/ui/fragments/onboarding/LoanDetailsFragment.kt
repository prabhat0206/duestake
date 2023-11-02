package com.example.duestake.ui.fragments.onboarding

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.duestake.R
import com.example.duestake.data.Constant
import com.example.duestake.data.Result
import com.example.duestake.data.onboarding.LoanDetails
import com.example.duestake.data.user.UserData
import com.example.duestake.databinding.FragmentLoanDetailsBinding
import com.example.duestake.ui.activities.OnboardingActivity
import com.example.duestake.ui.fragments.BaseFragment
import com.example.duestake.ui.viewmodels.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoanDetailsFragment : BaseFragment() {

    private var _binding: FragmentLoanDetailsBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<OnboardingViewModel>()

    private lateinit var sharedPreference: SharedPreferences

    private var loanTypeText = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoanDetailsBinding.inflate(layoutInflater, container, false)

        init()

        return binding.root
    }

    private fun init() {
        sharedPreference =
            requireContext().getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        if (sharedPreference.getBoolean(Constant.LOAN_DETAILS, false)) {
            viewModel.userData.observe( viewLifecycleOwner ) {
                if (it != null) {
                    displayData(it)
                }
            }
        }

        setLoanTypeSpinner()

        binding.loanTypeSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    val number = p0?.getItemAtPosition(p2).toString()
                    loanTypeText = number
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }

        binding.loanAmount.filters = arrayOf(InputFilter.LengthFilter(7))

        binding.loanTenure.filters = arrayOf(InputFilter.LengthFilter(3))

        (activity as OnboardingActivity).checkFragment(Constant.LOAN_DETAILS)

        viewModel.loanDetailsResource.observe( viewLifecycleOwner ) { response ->
            when (response) {
                is Result.Success -> {
                    if (response.data.success) {
                        (activity as OnboardingActivity).hideProgressBar()
                        hideErrorMessage(binding.errorMessageCL)
                        saveDataLocally()
                    } else {
                        (activity as OnboardingActivity).hideProgressBar()
                        hideErrorMessage(binding.errorMessageCL)
                        Toast.makeText(
                            activity,
                            "An error occurred: ${response.data.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                is Result.Error -> {
                    (activity as OnboardingActivity).hideProgressBar()
                    response.exception.let { message ->
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_LONG)
                            .show()
                        showErrorMessage(
                            message.message.toString(),
                            binding.errorMessageCL,
                            binding.itemErrorMessage.tvErrorMessage
                        )
                    }
                }
                is Result.Loading -> {
                    (activity as OnboardingActivity).showProgressBar()
                }
            }
        }

        binding.btnAskForLoan.setOnClickListener {
            checkLoanInformation()
        }

        binding.itemErrorMessage.btnRetry.setOnClickListener {
            checkLoanInformation()
        }

        binding.itemErrorMessage.btnCancel.setOnClickListener {
            hideErrorMessage(binding.errorMessageCL)
        }
    }

    private fun displayData(userData: UserData) {
        binding.loanAmount.setText(userData.userLoanAmount.toString())
        binding.loanTenure.setText(userData.userLoanTenure.toString())
        var loanTypePosition = 0
        for (i in loanType.indices) {
            if (loanType[i] == userData.userLoanType) {
                loanTypePosition = i
                break
            }
        }
        binding.loanTypeSpinner.setSelection(loanTypePosition)
    }

    private fun setLoanTypeSpinner() {
        val arrayAdapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(
            requireContext(), R.layout.spinner_list, loanType as List<Any?>
        )

        arrayAdapter.setDropDownViewResource(R.layout.spinner_list)

        binding.loanTypeSpinner.adapter = arrayAdapter
    }

    private fun checkLoanInformation() {
        if (binding.loanAmount.text.toString() != "" && binding.loanTenure.text.toString() != "") {
            if (binding.loanAmount.text.toString()
                    .toInt() < 5000000 && binding.loanAmount.text.toString().toInt() > 5000
            ) {
                if (binding.loanTenure.text.toString().toInt() < 360) {
                    val userId = sharedPreference.getString(Constant.USER_ID, "")
                    val loanDetails = LoanDetails(
                        userLoanAmount = binding.loanAmount.text.toString().toInt(),
                        userLoanTenure = binding.loanTenure.text.toString().toInt(),
                        userLoanType = loanTypeText
                    )
                    viewModel.sendLoanDetails(userId!!, loanDetails)
                    markButtonDisable(binding.btnAskForLoan)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Loan Tenure can't me more the 360 months",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Loan amount should between 5,000 - 50,00,000 ₹",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                requireContext(), "Please enter all details properly", Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun saveDataLocally() {
        viewModel.userData.observe( viewLifecycleOwner ) {
            if (it != null) {
                val newUserData = it.copy(
                    _id = "1",
                    userLoanAmount = binding.loanAmount.text.toString().toInt(),
                    userLoanTenure = binding.loanTenure.text.toString().toInt(),
                    userLoanType = loanTypeText
                )
                viewModel.insert(newUserData)
                sendLoanDetails()
            } else {
                val newUserData = UserData(
                    _id = "1",
                    userLoanAmount = binding.loanAmount.text.toString().toInt(),
                    userLoanTenure = binding.loanTenure.text.toString().toInt(),
                    userLoanType = loanTypeText
                )
                viewModel.insert(newUserData)
                sendLoanDetails()
            }
        }
    }

    private fun sendLoanDetails() {
        val editor = sharedPreference.edit()
        editor.putBoolean(Constant.LOAN_DETAILS, true)
        editor.apply()
        (activity as OnboardingActivity).checkFragment(Constant.LOAN_DETAILS)
        (activity as OnboardingActivity).secondFragment()
    }
}