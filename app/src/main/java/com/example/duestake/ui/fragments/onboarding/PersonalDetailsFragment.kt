package com.example.duestake.ui.fragments.onboarding

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
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
import com.example.duestake.data.onboarding.PersonalDetails
import com.example.duestake.data.user.UserData
import com.example.duestake.databinding.FragmentPersonalDetailsBinding
import com.example.duestake.ui.activities.OnboardingActivity
import com.example.duestake.ui.fragments.BaseFragment
import com.example.duestake.ui.viewmodels.OnboardingViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class PersonalDetailsFragment : BaseFragment() {

    private var _binding: FragmentPersonalDetailsBinding? = null

    private val binding get() = _binding!!

    private var genderText = ""

    private var maritalStatusText = ""

    private var qualificationText = ""

    private val viewModel by viewModels<OnboardingViewModel>()

    private lateinit var sharedPreference: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalDetailsBinding.inflate(layoutInflater, container, false)

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
        }

        checkEnableTextView()

        (activity as OnboardingActivity).checkFragment(Constant.PERSONAL_DETAILS)

        setDatePicker()

        setGenderSpinner()

        setMaritalStatusSpinner()

        setQualificationSpinner()

        binding.genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val number = p0?.getItemAtPosition(p2).toString()
                genderText = number
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        binding.maritalStatusSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    val number = p0?.getItemAtPosition(p2).toString()
                    maritalStatusText = number
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }

        binding.qualificationSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    val number = p0?.getItemAtPosition(p2).toString()
                    qualificationText = number
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }

        viewModel.personalDetailsResource.observe( viewLifecycleOwner ) { response ->
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

        binding.btnContinue.setOnClickListener {
            checkPersonalInformation()
        }

        binding.itemErrorMessage.btnRetry.setOnClickListener {
            checkPersonalInformation()
        }
    }


    private fun checkEnableTextView() {
        if (sharedPreference.getBoolean(Constant.IS_PAN_DETAILS_FETCHED, false)) {
            binding.name.isEnabled = false
            binding.dateOfBirth.isEnabled = false
            binding.dateOfBirth.setOnClickListener(null)
        }
    }

    private fun checkPersonalInformation() {
        if (binding.name.text.toString() != "" && binding.dateOfBirth.text.toString() != "" && binding.monthlyExpenses.text.toString() != "" && binding.fatherName.text.toString() != "" && binding.motherName.text.toString() != "") {
            val userId = sharedPreference.getString(Constant.USER_ID, "")
            val personalDetails = PersonalDetails(
                userFullName = binding.name.text.toString(),
                userGender = genderText,
                userDOB = binding.dateOfBirth.text.toString(),
                userMaritalStatus = maritalStatusText,
                userQualification = qualificationText,
                userMonthlyExpense = binding.monthlyExpenses.text.toString().toInt(),
                userFatherName = binding.fatherName.text.toString(),
                userMotherName = binding.motherName.text.toString()
            )
            if (isInternetConnected) {
                savePersonalInformation(personalDetails)
                markButtonDisable(binding.btnContinue)
                viewModel.sendPersonalDetails(userId!!, personalDetails)
            } else {
                savePersonalInformation(personalDetails)
                markButtonDisable(binding.btnContinue)
                viewModel.sendPersonalDetails(userId!!, personalDetails)
            }
        } else {
            Toast.makeText(
                requireContext(), "Please enter all details properly", Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun savePersonalInformation(personalDetails: PersonalDetails) {
        val editor = sharedPreference.edit()
        editor.putString(Constant.USER_NAME, personalDetails.userFullName)
        editor.apply()
    }

    private fun setDatePicker() {
        val dateOfBirth =
            MaterialDatePicker.Builder.datePicker().setTitleText("Select Date Of Birth").build()

        binding.dateOfBirth.setOnClickListener {
            dateOfBirth.show(childFragmentManager, "DATE_PICKER")
        }

        dateOfBirth.addOnPositiveButtonClickListener {
            val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            val date = sdf.format(it)
            binding.dateOfBirth.setText(date)
        }
    }

    private fun setGenderSpinner() {
        val arrayAdapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(
            requireContext(), R.layout.spinner_list, gender as List<Any?>
        )

        arrayAdapter.setDropDownViewResource(R.layout.spinner_list)

        binding.genderSpinner.adapter = arrayAdapter
    }

    private fun setMaritalStatusSpinner() {
        val arrayAdapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(
            requireContext(), R.layout.spinner_list, maritalStatus as List<Any?>
        )

        arrayAdapter.setDropDownViewResource(R.layout.spinner_list)

        binding.maritalStatusSpinner.adapter = arrayAdapter
    }

    private fun setQualificationSpinner() {
        val arrayAdapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(
            requireContext(), R.layout.spinner_list, qualification as List<Any?>
        )

        arrayAdapter.setDropDownViewResource(R.layout.spinner_list)

        binding.qualificationSpinner.adapter = arrayAdapter
    }

    private fun displayData(userData: UserData) {
        binding.name.setText(userData.userFullName)
        binding.dateOfBirth.setText(userData.userDOB)
        binding.monthlyExpenses.setText(userData.userMonthlyExpense.toString())
        binding.fatherName.setText(userData.userFatherName)
        binding.motherName.setText(userData.userMotherName)
        var maritalStatusPosition = 0
        for (i in maritalStatus.indices) {
            if (maritalStatus[i] == userData.userMaritalStatus) {
                maritalStatusPosition = i
                break
            }
        }
        var genderPosition = 0
        for (i in gender.indices) {
            if (gender[i] == userData.userGender) {
                genderPosition = i
                break
            }
        }
        var qualificationPosition = 0
        for (i in qualification.indices) {
            if (qualification[i] == userData.userQualification) {
                qualificationPosition = i
                break
            }
        }
        binding.genderSpinner.setSelection(genderPosition)
        binding.maritalStatusSpinner.setSelection(maritalStatusPosition)
        binding.qualificationSpinner.setSelection(qualificationPosition)
    }

    private fun saveDataLocally() {
        viewModel.userData.observe( viewLifecycleOwner ) {
            if (it != null) {
                val newUserData = it.copy(
                    _id = "1",
                    userFullName = binding.name.text.toString(),
                    userGender = genderText,
                    userDOB = binding.dateOfBirth.text.toString(),
                    userMaritalStatus = maritalStatusText,
                    userQualification = qualificationText,
                    userMonthlyExpense = binding.monthlyExpenses.text.toString().toInt(),
                    userFatherName = binding.fatherName.text.toString(),
                    userMotherName = binding.motherName.text.toString()
                )
                viewModel.insert(newUserData)
                sendPersonalDetails()
            } else {
                val newUserData = UserData(
                    _id = "1",
                    userFullName = binding.name.text.toString(),
                    userGender = genderText,
                    userDOB = binding.dateOfBirth.text.toString(),
                    userMaritalStatus = maritalStatusText,
                    userQualification = qualificationText,
                    userMonthlyExpense = binding.monthlyExpenses.text.toString().toInt(),
                    userFatherName = binding.fatherName.text.toString(),
                    userMotherName = binding.motherName.text.toString()
                )
                viewModel.insert(newUserData)
                sendPersonalDetails()
            }
        }
    }

    private fun sendPersonalDetails() {
        val editor = sharedPreference.edit()
        editor.putBoolean(Constant.PERSONAL_DETAILS, true)
        editor.apply()
        (activity as OnboardingActivity).checkFragment(Constant.ADDRESS_VERIFICATION)
        (activity as OnboardingActivity).thirdFragment()
    }
}