package com.example.duestake.ui.fragments.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.duestake.R
import com.example.duestake.data.Constant
import com.example.duestake.databinding.FragmentSplashBinding
import com.example.duestake.ui.activities.HomeActivity
import com.example.duestake.ui.activities.OnboardingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(layoutInflater, container, false)

        CoroutineScope(Dispatchers.IO).launch {
            timer()
        }

        return binding.root
    }

    private suspend fun timer() {
        withContext(Dispatchers.Main) {
            binding.logo.animate().alpha(1f).duration = 700
        }
        delay(1000)
        withContext(Dispatchers.Main) {
            binding.logo.animate().alpha(0f).duration = 700
        }
        delay(1000)
        val sharedPreference =
            requireContext().getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        if (sharedPreference.getString(Constant.USER_ID, "") != "") {
            if (sharedPreference.getBoolean(Constant.LOAN_DETAILS, false) &&
                sharedPreference.getBoolean(Constant.PERSONAL_DETAILS, false) &&
                sharedPreference.getBoolean(Constant.ADDRESS_VERIFICATION, false) &&
                sharedPreference.getBoolean(Constant.KYC_DETAILS, false) &&
                sharedPreference.getBoolean(Constant.EMPLOYMENT_DETAILS, false)) {
                val intent = Intent(requireContext(), HomeActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            } else {
                val intent = Intent(requireContext(), OnboardingActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        } else {
            withContext(Dispatchers.Main) {
                findNavController().navigate(R.id.action_splashFragment_to_signUpFragment)
            }
        }
    }

}