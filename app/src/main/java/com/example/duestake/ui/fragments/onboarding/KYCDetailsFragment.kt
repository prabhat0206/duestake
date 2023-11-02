package com.example.duestake.ui.fragments.onboarding

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.duestake.R
import com.example.duestake.data.Constant
import com.example.duestake.data.Result
import com.example.duestake.data.emptra.PanNumber
import com.example.duestake.data.emptra.PanResult
import com.example.duestake.data.onboarding.KYCDetails
import com.example.duestake.data.onboarding.UpdatePanDetails
import com.example.duestake.data.user.UserData
import com.example.duestake.databinding.FragmentKycDetailsBinding
import com.example.duestake.ui.activities.OnboardingActivity
import com.example.duestake.ui.fragments.BaseFragment
import com.example.duestake.ui.viewmodels.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KYCDetailsFragment : BaseFragment() {

    private var _binding: FragmentKycDetailsBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<OnboardingViewModel>()

    private lateinit var sharedPreference: SharedPreferences

    private var userId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKycDetailsBinding.inflate(layoutInflater, container, false)

        init()

        return binding.root
    }

    private fun init() {
        checkNetworkConnection(application = requireActivity().application)

        sharedPreference =
            requireContext().getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)

        viewModel.userData.observe( viewLifecycleOwner ) {
            if (it != null) {
                displayData(it)
            }
            if (sharedPreference.getBoolean(Constant.IS_PAN_DETAILS_FETCHED, false)) {
                displayData(it)
            }
        }

        userId = sharedPreference.getString(Constant.USER_ID, "").toString()

        checkEnableTextView()

        (activity as OnboardingActivity).checkFragment(Constant.KYC_DETAILS)

        binding.ifscCode.filters = arrayOf(InputFilter.LengthFilter(11))

        binding.panNumber.filters = arrayOf(InputFilter.LengthFilter(10))

        viewModel.panDetailsResource.observe( viewLifecycleOwner ) { response ->
            when (response) {
                is Result.Success -> {
                    (activity as OnboardingActivity).hideProgressBar()
                    hideErrorMessage(binding.errorMessageCL)
                    response.data.result.let { setAadharNumber(it) }
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

        viewModel.kycDetailsResource.observe( viewLifecycleOwner ) { response ->
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
                            activity, "An error occurred: ${response.data.message}", Toast.LENGTH_LONG
                        ).show()
                    }
                }
                is Result.Error -> {
                    (activity as OnboardingActivity).hideProgressBar()
                    response.exception.let { message ->
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_LONG)
                            .show()
                        showErrorMessage(
                            message.message.toString(), binding.errorMessageCL, binding.itemErrorMessage.tvErrorMessage
                        )
                    }
                }
                is Result.Loading -> {
                    (activity as OnboardingActivity).showProgressBar()
                }
            }
        }

        viewModel.updatePanDetailsResource.observe( viewLifecycleOwner ) { response ->
            when (response) {
                is Result.Success -> {
                    if (response.data.success) {
                        (activity as OnboardingActivity).hideProgressBar()
                        hideErrorMessage(binding.errorMessageCL)
                    } else {
                        (activity as OnboardingActivity).hideProgressBar()
                        hideErrorMessage(binding.errorMessageCL)
                        Toast.makeText(
                            activity, "An error occurred: ${response.data.message}", Toast.LENGTH_LONG
                        ).show()
                    }
                }
                is Result.Error -> {
                    (activity as OnboardingActivity).hideProgressBar()
                    response.exception.let { message ->
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_LONG)
                            .show()
                        showErrorMessage(
                            message.message.toString(), binding.errorMessageCL, binding.itemErrorMessage.tvErrorMessage
                        )
                    }
                }
                is Result.Loading -> {
                    (activity as OnboardingActivity).showProgressBar()
                }
            }
        }

        binding.btnContinue.setOnClickListener {
            checkKYCDetails()
        }

        binding.itemErrorMessage.btnRetry.setOnClickListener {
            checkKYCDetails()
        }
    }

    private fun displayData(userData: UserData) {
        binding.bankName.setText(userData.userBankName)
        binding.ifscCode.setText(userData.userBankIFSCCode)
        binding.panNumber.setText(userData.userPANNumber)
        binding.aadharNumber.setText(userData.userAdharCardNo.toString())
    }

    private fun setAadharNumber(panResult: PanResult) {
        if (!sharedPreference.getBoolean(Constant.IS_PAN_DETAILS_FETCHED, false)) {
            viewModel.updatePanDetails(
                userId, UpdatePanDetails(
                    userAdharCardNo = panResult.data.AADHAR_NUM,
                    userCity = panResult.data.CITY,
                    userDOB = panResult.data.DOB,
                    userGender = panResult.data.GENDER,
                    userPANNumber = panResult.data.PAN,
                    userState = panResult.data.STATE,
                    userPincode = panResult.data.PINCODE.toInt(),
                    userFullName = "${panResult.data.FIRST_NAME} ${panResult.data.MIDDLE_NAME} ${panResult.data.LAST_NAME}"
                )
            )
            viewModel.userData.observe( viewLifecycleOwner ) {
                if (it != null) {
                    val newUserData = it.copy(
                        _id = "1",
                        userAdharCardNo = panResult.data.AADHAR_NUM,
                        userCity = panResult.data.CITY,
                        userDOB = panResult.data.DOB,
                        userGender = panResult.data.GENDER,
                        userPANNumber = panResult.data.PAN,
                        userState = panResult.data.STATE,
                        userFullName = "${panResult.data.FIRST_NAME} ${panResult.data.MIDDLE_NAME} ${panResult.data.LAST_NAME}",
                        userPincode = panResult.data.PINCODE.toInt()
                    )
                    viewModel.insert(userData = newUserData)
                } else {
                    val newUserData = UserData(
                        _id = "1",
                        userAdharCardNo = panResult.data.AADHAR_NUM,
                        userCity = panResult.data.CITY,
                        userDOB = panResult.data.DOB,
                        userGender = panResult.data.GENDER,
                        userPANNumber = panResult.data.PAN,
                        userState = panResult.data.STATE,
                        userFullName = "${panResult.data.FIRST_NAME} ${panResult.data.MIDDLE_NAME} ${panResult.data.LAST_NAME}",
                        userPincode = panResult.data.PINCODE.toInt()
                    )
                    viewModel.insert(userData = newUserData)
                }
            }
        }

        binding.aadharNumber.setText(panResult.data.AADHAR_NUM.toString())
        val editor = sharedPreference.edit()
        editor.putBoolean(Constant.IS_PAN_DETAILS_FETCHED, true)
        editor.apply()
        checkEnableTextView()
    }

    private fun checkEnableTextView() {
        if (sharedPreference.getBoolean(Constant.IS_PAN_DETAILS_FETCHED, false)) {
            binding.aadharNumber.isEnabled = false
            binding.panNumber.isEnabled = false
        } else {
            binding.panNumber.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence, start: Int, count: Int, after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.length == 10) {
                        if (isInternetConnected) {
                            fetchPanDetails()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "An error occurred: Internet not available",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }

                override fun afterTextChanged(s: Editable) {
                }
            })
        }
    }

    private fun fetchPanDetails() {
        if (!sharedPreference.getBoolean(Constant.IS_PAN_DETAILS_FETCHED, false)) {
            viewModel.panDetails(PanNumber(binding.panNumber.text.toString()))
        }
    }

    private fun checkKYCDetails() {
        if (binding.bankName.text.toString() != "" && binding.ifscCode.text.toString() != "" && binding.panNumber.text.toString() != "" && binding.aadharNumber.text.toString() != "") {
            val kycDetails = KYCDetails(
                userAdharCardNo = binding.aadharNumber.text.toString().toLong(),
                userBankName = binding.bankName.text.toString(),
                userBankIFSCCode = binding.ifscCode.text.toString(),
                userPANNumber = binding.panNumber.text.toString()
            )
            if (isInternetConnected) {
                viewModel.sendKYCDetails(userId, kycDetails)
            } else {
                Toast.makeText(
                    requireContext(), "An error occurred: Internet not available", Toast.LENGTH_LONG
                ).show()
            }

            markButtonDisable(binding.btnContinue)
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
                    userAdharCardNo = binding.aadharNumber.text.toString().toLong(),
                    userBankName = binding.bankName.text.toString(),
                    userBankIFSCCode = binding.ifscCode.text.toString(),
                    userPANNumber = binding.panNumber.text.toString()
                )
                viewModel.insert(userData = newUserData)
                sendKYCDetails()
            } else {
                val newUserData = UserData(
                    _id = "1",
                    userAdharCardNo = binding.aadharNumber.text.toString().toLong(),
                    userBankName = binding.bankName.text.toString(),
                    userBankIFSCCode = binding.ifscCode.text.toString(),
                    userPANNumber = binding.panNumber.text.toString()
                )
                viewModel.insert(userData = newUserData)
                sendKYCDetails()
            }
        }
    }


    private fun sendKYCDetails() {
        val editor = sharedPreference.edit()
        editor.putBoolean(Constant.KYC_DETAILS, true)
        editor.apply()
        (activity as OnboardingActivity).checkFragment(Constant.ADDRESS_VERIFICATION)
        (activity as OnboardingActivity).fifthFragment()
    }
}