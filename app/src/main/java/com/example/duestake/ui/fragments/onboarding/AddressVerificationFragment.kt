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
import com.example.duestake.R
import com.example.duestake.data.Constant
import com.example.duestake.data.Result
import com.example.duestake.data.onboarding.AddressVerification
import com.example.duestake.data.user.UserData
import com.example.duestake.databinding.FragmentAddressVerficationBinding
import com.example.duestake.ui.activities.OnboardingActivity
import com.example.duestake.ui.fragments.BaseFragment
import com.example.duestake.ui.viewmodels.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressVerificationFragment : BaseFragment() {

    private var _binding: FragmentAddressVerficationBinding? = null

    private val binding get() = _binding!!

    private var residentTypeText = ""

    private val viewModel by viewModels<OnboardingViewModel>()

    private lateinit var sharedPreference: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddressVerficationBinding.inflate(layoutInflater, container, false)

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

        checkEnableTextView()

        (activity as OnboardingActivity).checkFragment(Constant.ADDRESS_VERIFICATION)

        setResidentTypeSpinner()

        binding.pinCode.filters = arrayOf(InputFilter.LengthFilter(6))

        binding.residentTypeSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    val number = p0?.getItemAtPosition(p2).toString()
                    residentTypeText = number
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }

        viewModel.addressVerificationResult.observe( viewLifecycleOwner ){ response ->
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

        binding.btnContinue.setOnClickListener {
            checkAddressVerification()
        }

        binding.itemErrorMessage.btnRetry.setOnClickListener {
            checkAddressVerification()
        }
    }

    private fun checkEnableTextView() {
        if (sharedPreference.getBoolean(Constant.IS_PAN_DETAILS_FETCHED, false)) {
            binding.pinCode.isEnabled = false
            binding.state.isEnabled = false
            binding.city.isEnabled = false
        }
    }

    private fun displayData(userData: UserData) {
        binding.pinCode.setText(userData.userPincode.toString())
        binding.state.setText(userData.userState.toString())
        binding.city.setText(userData.userCity.toString())
        var residentTypePosition = 0
        for (i in residentType.indices) {
            if (residentType[i] == userData.userResidentType) {
                residentTypePosition = i
                break
            }
        }
        binding.residentTypeSpinner.setSelection(residentTypePosition)
    }

    private fun checkAddressVerification() {
        if (binding.pinCode.text.toString() != "" && binding.state.text.toString() != "" && binding.city.text.toString() != "") {
            val userId = sharedPreference.getString(Constant.USER_ID, "")
            val addressVerification = AddressVerification(
                userPincode = binding.pinCode.text.toString().toInt(),
                userState = binding.state.text.toString(),
                userCity = binding.city.text.toString(),
                userResidentType = residentTypeText
            )
            if (isInternetConnected) {
                markButtonDisable(binding.btnContinue)
                viewModel.sendAddressVerification(userId!!, addressVerification)
            } else {
                Toast.makeText(
                    requireContext(),
                    "An error occurred: Internet not available",
                    Toast.LENGTH_LONG
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
                    userPincode = binding.pinCode.text.toString().toInt(),
                    userState = binding.state.text.toString(),
                    userCity = binding.city.text.toString(),
                    userResidentType = residentTypeText
                )
                viewModel.insert(newUserData)
                sendAddressVerification()
            } else {
                val newUserData = UserData(
                    _id = "1",
                    userPincode = binding.pinCode.text.toString().toInt(),
                    userState = binding.state.text.toString(),
                    userCity = binding.city.text.toString(),
                    userResidentType = residentTypeText
                )
                viewModel.insert(newUserData)
                sendAddressVerification()
            }
        }
    }

    private fun sendAddressVerification() {
        val editor = sharedPreference.edit()
        editor.putBoolean(Constant.ADDRESS_VERIFICATION, true)
        editor.apply()
        (activity as OnboardingActivity).checkFragment(Constant.ADDRESS_VERIFICATION)
        (activity as OnboardingActivity).fourthFragment()
    }

    private fun setResidentTypeSpinner() {
        val residentType =
            mutableListOf("Self owned", "Owned by Parents", "Owned by siblings", "Rental")

        val arrayAdapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(
            requireContext(), R.layout.spinner_list, residentType as List<Any?>
        )

        arrayAdapter.setDropDownViewResource(R.layout.spinner_list)

        binding.residentTypeSpinner.adapter = arrayAdapter
    }
}