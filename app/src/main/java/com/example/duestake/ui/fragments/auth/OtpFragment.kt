package com.example.duestake.ui.fragments.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.duestake.R
import com.example.duestake.data.Constant
import com.example.duestake.data.Result
import com.example.duestake.data.auth.LoginUser
import com.example.duestake.data.auth.Otp
import com.example.duestake.data.auth.RegisterUser
import com.example.duestake.data.auth.UserInformation
import com.example.duestake.databinding.FragmentOtpBinding
import com.example.duestake.ui.activities.OnboardingActivity
import com.example.duestake.ui.fragments.BaseFragment
import com.example.duestake.ui.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtpFragment : BaseFragment() {

    private var _binding: FragmentOtpBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<AuthViewModel>()

    private val args: OtpFragmentArgs by navArgs()

    private var phoneNumber = ""

    private var login = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOtpBinding.inflate(layoutInflater, container, false)

        init()

        return binding.root
    }

    private fun init() {
        checkNetworkConnection(application = requireActivity().application)

        editTextInput()

        phoneNumber = args.phoneNumber

        login = args.login

        binding.loginMessageTextView.text =
            requireContext().getString(R.string.haven_t_got_the_confirmation_code_yet, phoneNumber)

        binding.changeNumberTextView.setOnClickListener {
            if (login) {
                findNavController().navigate(R.id.action_otpFragment_to_loginFragment)
            } else {
                findNavController().navigate(R.id.action_otpFragment_to_signUpFragment)
            }
        }

        binding.changeNumberTextView.setOnClickListener {
            if (login) {
                findNavController().navigate(R.id.action_otpFragment_to_loginFragment)
            } else {
                findNavController().navigate(R.id.action_otpFragment_to_signUpFragment)
            }
        }

        binding.resendOtp.setOnClickListener {
            if (login) {
                if (isInternetConnected) {
                    val otp = Otp("+91${phoneNumber}")
                    viewModel.sendLoginOtp(otp)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "An error occurred: Internet not available",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                if (isInternetConnected) {
                    val otp = Otp("+91${phoneNumber}")
                    viewModel.sendRegisterOtp(otp)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "An error occurred: Internet not available",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        binding.btnConfirmOtp.setOnClickListener {
            if (binding.etC1.text.toString().trim().isEmpty() || binding.etC2.text.toString().trim()
                    .isEmpty() || binding.etC3.text.toString().trim()
                    .isEmpty() || binding.etC4.text.toString().trim()
                    .isEmpty() || binding.etC5.text.toString().trim()
                    .isEmpty() || binding.etC6.text.toString().trim().isEmpty()
            ) {
                Toast.makeText(requireContext(), "OTP is not Valid!", Toast.LENGTH_SHORT).show()
            } else {
                showProgressBar(binding.progressBarCL)
                markButtonDisable(binding.btnConfirmOtp)
                val code = binding.etC1.text.toString().trim() + binding.etC2.text.toString()
                    .trim() + binding.etC3.text.toString().trim() + binding.etC4.text.toString()
                    .trim() + binding.etC5.text.toString().trim() + binding.etC6.text.toString()
                    .trim()
                if (login) {
                    loginUser(code)
                } else {
                    registerUser(code)
                }
            }
        }

        viewModel.loginOtpResource.observe( viewLifecycleOwner ) { response ->
            when (response) {
                is Result.Success -> {
                    if (response.data.success) {
                        hideProgressBar(binding.progressBarCL)
                        hideErrorMessage(binding.errorMessageCL)
                    } else {
                        hideProgressBar(binding.progressBarCL)
                        hideErrorMessage(binding.errorMessageCL)
                        Toast.makeText(
                            activity, "An error occurred: ${response.data.message}", Toast.LENGTH_LONG
                        ).show()
                    }
                }
                is Result.Error -> {
                    hideProgressBar(binding.progressBarCL)
                    response.exception.let { message ->
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_LONG)
                            .show()
                        showErrorMessage(
                            message.message.toString(), binding.errorMessageCL, binding.itemErrorMessage.tvErrorMessage
                        )
                    }
                }
                is Result.Loading -> {
                    showProgressBar(binding.progressBarCL)
                }
            }
        }

        viewModel.registerOtpResource.observe( viewLifecycleOwner ) { response ->
            when (response) {
                is Result.Success -> {
                    if (response.data.success) {
                        hideProgressBar(binding.progressBarCL)
                        hideErrorMessage(binding.errorMessageCL)
                    } else {
                        hideProgressBar(binding.progressBarCL)
                        hideErrorMessage(binding.errorMessageCL)
                        Toast.makeText(
                            activity,
                            "An error occurred: ${response.data.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                is Result.Error -> {
                    hideProgressBar(binding.progressBarCL)
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
                    showProgressBar(binding.progressBarCL)
                }
            }
        }

        viewModel.registerUserResource.observe( viewLifecycleOwner ) { response ->
            when (response) {
                is Result.Success -> {
                    if (response.data.success){
                        hideProgressBar(binding.progressBarCL)
                        hideErrorMessage(binding.errorMessageCL)
                        registerUserSuccessfully(response.data)
                    } else {
                        hideProgressBar(binding.progressBarCL)
                        hideErrorMessage(binding.errorMessageCL)
                        Toast.makeText(
                            activity,
                            "An error occurred: ${response.data.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                is Result.Error -> {
                    hideProgressBar(binding.progressBarCL)
                    response.exception.let { message ->
                        Toast.makeText(
                            requireContext(), "An error occurred: $message", Toast.LENGTH_LONG
                        ).show()
                        showErrorMessage(
                            message.message.toString(), binding.errorMessageCL, binding.itemErrorMessage.tvErrorMessage
                        )
                    }
                }
                is Result.Loading -> {
                    showProgressBar(binding.progressBarCL)
                }
            }
        }

        viewModel.loginUserResource.observe( viewLifecycleOwner ) { response ->
            when (response) {
                is Result.Success -> {
                    if (response.data.success) {
                        hideProgressBar(binding.progressBarCL)
                        hideErrorMessage(binding.errorMessageCL)
                        registerUserSuccessfully(response.data)
                    } else {
                        hideProgressBar(binding.progressBarCL)
                        hideErrorMessage(binding.errorMessageCL)
                        Toast.makeText(
                            activity,
                            "An error occurred: ${response.data.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                is Result.Error -> {
                    hideProgressBar(binding.progressBarCL)
                    response.exception.let { message ->
                        Toast.makeText(
                            requireContext(), "An error occurred: $message", Toast.LENGTH_LONG
                        ).show()
                        showErrorMessage(
                            message.message.toString(), binding.errorMessageCL, binding.itemErrorMessage.tvErrorMessage
                        )
                    }
                }
                is Result.Loading -> {
                    showProgressBar(binding.progressBarCL)
                }
            }
        }
    }

    private fun loginUser(code: String) {
        if (isInternetConnected) {
            val loginUser = LoginUser(userContactNo = "+91${phoneNumber}", verifyCode = code)
            viewModel.loginUser(loginUser)
        } else {
            Toast.makeText(
                requireContext(),
                "An error occurred: Internet not available",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun registerUserSuccessfully(data: UserInformation) {
        val sharedPreference =
            requireContext().getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putString(Constant.USER_ID, data.data)
        editor.putString(Constant.USER_PHONE_NUMBER, phoneNumber)
        editor.apply()
        val intent = Intent(requireContext(), OnboardingActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    private fun registerUser(code: String) {
        if (isInternetConnected) {
            val registerUser = RegisterUser(userContactNo = "+91${phoneNumber}", verifyCode = code)
            viewModel.registerUser(registerUser)
        } else {
            Toast.makeText(
                requireContext(),
                "An error occurred: Internet not available",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun editTextInput() {
        binding.etC1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                binding.etC2.requestFocus()
            }

            override fun afterTextChanged(s: Editable) {}
        })
        binding.etC2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                binding.etC3.requestFocus()
            }

            override fun afterTextChanged(s: Editable) {}
        })
        binding.etC3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                binding.etC4.requestFocus()
            }

            override fun afterTextChanged(s: Editable) {}
        })
        binding.etC4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                binding.etC5.requestFocus()
            }

            override fun afterTextChanged(s: Editable) {}
        })
        binding.etC5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                binding.etC6.requestFocus()
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }
}