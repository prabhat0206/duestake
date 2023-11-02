package com.example.duestake.ui.fragments.onboarding

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.duestake.R
import com.example.duestake.adapter.CompanyAdapter
import com.example.duestake.data.Constant
import com.example.duestake.data.Result
import com.example.duestake.data.emptra.CompanyName
import com.example.duestake.data.onboarding.EmploymentDetails
import com.example.duestake.data.user.UserData
import com.example.duestake.databinding.FragmentEmploymentDetailsBinding
import com.example.duestake.ui.activities.HomeActivity
import com.example.duestake.ui.activities.OnboardingActivity
import com.example.duestake.ui.fragments.BaseFragment
import com.example.duestake.ui.viewmodels.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmploymentDetailsFragment : BaseFragment() {

    private var _binding: FragmentEmploymentDetailsBinding? = null

    private val binding get() = _binding!!

    private var employmentTypeText = ""

    private var modeOfSalaryText = ""

    private var companyTypeText = ""

    private val viewModel by viewModels<OnboardingViewModel>()

    private lateinit var sharedPreference: SharedPreferences

    private lateinit var companyAdapter: CompanyAdapter

    private var fetchCompanyName = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmploymentDetailsBinding.inflate(layoutInflater, container, false)

        init()

        return binding.root
    }

    private fun init() {
        checkNetworkConnection(application = requireActivity().application)

        sharedPreference =
            requireContext().getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        if (sharedPreference.getBoolean(Constant.EMPLOYMENT_DETAILS, false)) {
            viewModel.userData.observe( viewLifecycleOwner ) {
                if (it != null) {
                    displayData(it)
                }
            }
        }

        (activity as OnboardingActivity).checkFragment(Constant.EMPLOYMENT_DETAILS)

        setEmploymentType()

        setModeOfSalarySpinner()

        setCompanyTypeSpinner()

        companyAdapter = CompanyAdapter(viewModel)

        binding.rvCompanyList.adapter = companyAdapter

        binding.modeOfSalarySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    val number = p0?.getItemAtPosition(p2).toString()
                    employmentTypeText = number
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }

        binding.modeOfSalarySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    val number = p0?.getItemAtPosition(p2).toString()
                    modeOfSalaryText = number
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }

        binding.companyTypeSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    val number = p0?.getItemAtPosition(p2).toString()
                    companyTypeText = number
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }

        viewModel.companyName.postValue("")

        viewModel.companyName.observe( viewLifecycleOwner ) {
            if (it != "") {
                fetchCompanyName = false
                binding.companyName.setText(it)
            }
            binding.companyName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence, start: Int, count: Int, after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.length >= 3 && fetchCompanyName) {
                        if (isInternetConnected) {
                            binding.rvCompanyList.visibility = View.VISIBLE
                            viewModel.companyList(CompanyName(binding.companyName.text.toString()))
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "An error occurred: Internet not available",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        binding.rvCompanyList.visibility = View.GONE
                    }
                    if (s.length < 3) {
                        fetchCompanyName = true
                    }
                }

                override fun afterTextChanged(s: Editable) {
                }
            })
        }


        viewModel.companyListResource.observe( viewLifecycleOwner ) { response ->
            when (response) {
                is Result.Success -> {
                    binding.rvProgressBar.visibility = View.GONE
                    hideErrorMessage(binding.errorMessageCL)
                    companyAdapter.differ.submitList(response.data.message.data)
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
                    binding.rvProgressBar.visibility = View.VISIBLE
                }
            }
        }

        viewModel.employmentDetailsResource.observe( viewLifecycleOwner ) { response ->
            when (response) {
                is Result.Success -> {
                    if (response.data.success) {
                        (activity as OnboardingActivity).hideProgressBar()
                        hideErrorMessage(binding.errorMessageCL)
                        sendPersonalDetails()
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
            checkEmploymentDetails()
        }

        binding.itemErrorMessage.btnRetry.setOnClickListener {
            checkEmploymentDetails()
        }
    }

    private fun displayData(userData: UserData) {
        binding.designation.setText(userData.userFullName)
        binding.monthlyIncome.setText(userData.userDOB)
        binding.companyName.setText(userData.userMonthlyExpense.toString())
        binding.industry.setText(userData.userFatherName)
        binding.experience.setText(userData.userMotherName)
        binding.pinCode.setText(userData.userMotherName)
        binding.state.setText(userData.userMotherName)
        binding.city.setText(userData.userMotherName)
    }

    private fun sendPersonalDetails() {
        val editor = sharedPreference.edit()
        editor.putBoolean(Constant.EMPLOYMENT_DETAILS, true)
        editor.apply()
        (activity as OnboardingActivity).checkFragment(Constant.EMPLOYMENT_DETAILS)
        val intent = Intent(requireContext(), HomeActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun checkEmploymentDetails() {
        if (binding.designation.text.toString() != "" && binding.industry.text.toString() != "" && binding.companyName.text.toString() != "" && binding.pinCode.text.toString() != "" && binding.state.text.toString() != "" && binding.designation.text.toString() != "" && binding.experience.text.toString() != "" && binding.monthlyIncome.text.toString() != "") {

            if (sharedPreference.getBoolean(
                    Constant.LOAN_DETAILS, false
                ) && sharedPreference.getBoolean(
                    Constant.PERSONAL_DETAILS, false
                ) && sharedPreference.getBoolean(
                    Constant.ADDRESS_VERIFICATION, false
                ) && sharedPreference.getBoolean(Constant.KYC_DETAILS, false)
            ) {

                if (binding.teamsAndConditionCheckBox.isChecked) {
                    val userId = sharedPreference.getString(Constant.USER_ID, "")
                    val employmentDetails = EmploymentDetails(
                        userCompanyCity = binding.city.text.toString(),
                        userCompanyIndustry = binding.industry.text.toString(),
                        userCompanyName = binding.companyName.text.toString(),
                        userCompanyPincode = binding.pinCode.text.toString().toInt(),
                        userCompanyState = binding.state.text.toString(),
                        userCompanyType = companyTypeText,
                        userDesignation = binding.designation.text.toString(),
                        userExperienceInCompany = binding.experience.text.toString(),
                        userModeOfSalary = modeOfSalaryText,
                        userMonthlyIncome = binding.monthlyIncome.text.toString().toInt(),
                        userSalariedStatus = employmentTypeText
                    )
                    markButtonDisable(binding.btnContinue)
                    viewModel.sendEmploymentDetails(userId!!, employmentDetails)
                } else {
                    Toast.makeText(
                        requireContext(), "Please accpet Terms and Condition", Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please enter all the previous details to continue",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                requireContext(), "Please enter all details properly", Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setEmploymentType() {
        val arrayAdapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(
            requireContext(), R.layout.spinner_list, employmentType as List<Any?>
        )

        arrayAdapter.setDropDownViewResource(R.layout.spinner_list)

        binding.employmentTypeSpinner.adapter = arrayAdapter
    }

    private fun setCompanyTypeSpinner() {
        val arrayAdapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(
            requireContext(), R.layout.spinner_list, companyType as List<Any?>
        )

        arrayAdapter.setDropDownViewResource(R.layout.spinner_list)

        binding.companyTypeSpinner.adapter = arrayAdapter
    }

    private fun setModeOfSalarySpinner() {
        val arrayAdapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(
            requireContext(), R.layout.spinner_list, modeOfSalary as List<Any?>
        )

        arrayAdapter.setDropDownViewResource(R.layout.spinner_list)

        binding.modeOfSalarySpinner.adapter = arrayAdapter
    }
}