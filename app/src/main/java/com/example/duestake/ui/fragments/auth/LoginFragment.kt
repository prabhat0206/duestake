package com.example.duestake.ui.fragments.auth

import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.duestake.R
import com.example.duestake.data.Result
import com.example.duestake.data.auth.Otp
import com.example.duestake.databinding.FragmentLoginBinding
import com.example.duestake.ui.fragments.BaseFragment
import com.example.duestake.ui.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)

        navigation()

        init()

        return binding.root
    }

    private fun init() {
        checkNetworkConnection(application = requireActivity().application)

        viewModel.loginOtpResource.observe( viewLifecycleOwner ) { response ->
            when (response) {
                is Result.Success -> {
                    if (response.data.success) {
                        hideProgressBar(binding.progressBarCL)
                        hideErrorMessage(binding.errorMessageCL)
                        otpSendSuccessfully()
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

        binding.mobileNumber.inputType = InputType.TYPE_CLASS_PHONE

        binding.mobileNumber.filters = arrayOf(InputFilter.LengthFilter(10))

        binding.btnLogin.setOnClickListener {
            if (binding.mobileNumber.text.toString() != ""
                && binding.mobileNumber.text.toString().length == 10) {
                if (isInternetConnected) {
                    val otp = Otp("+91${binding.mobileNumber.text.toString()}")
                    viewModel.sendLoginOtp(otp)
                    markButtonDisable(binding.btnLogin)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "An error occurred: Internet not available",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                Toast.makeText(
                    requireContext(), "Please enter a proper phone number", Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.itemErrorMessage.btnRetry.setOnClickListener {
            val otp = Otp(binding.mobileNumber.text.toString())
            if (isInternetConnected) {
                viewModel.sendLoginOtp(otp)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Internet connection not avaiable",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun otpSendSuccessfully() {
        val action = LoginFragmentDirections.actionLoginFragmentToOtpFragment(
            binding.mobileNumber.text.toString(), true
        )
        findNavController().navigate(action)
    }


    private fun navigation() {
        binding.signUpTextView.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
    }

}