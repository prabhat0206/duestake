package com.example.duestake.ui.activities

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.duestake.R
import com.example.duestake.data.Constant
import com.example.duestake.databinding.ActivityOnboardingBinding
import com.example.duestake.ui.fragments.onboarding.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        binding.firstCL.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, LoanDetailsFragment()).commit()
        }
        binding.secondCL.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, PersonalDetailsFragment()).commit()
        }
        binding.thirdCL.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, AddressVerificationFragment()).commit()
        }
        binding.fourthCL.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, KYCDetailsFragment()).commit()
        }
        binding.fifthCL.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, EmploymentDetailsFragment()).commit()
        }

    }

    fun secondFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, PersonalDetailsFragment()).commit()
    }

    fun thirdFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, AddressVerificationFragment()).commit()
    }

    fun fourthFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, KYCDetailsFragment()).commit()
    }

    fun fifthFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, EmploymentDetailsFragment()).commit()
    }

    fun checkFragment(fragmentName: String) {
        val sharedPreference = this.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        if (sharedPreference.getBoolean(Constant.LOAN_DETAILS, false)) {
            binding.firstTextView.visibility = View.GONE
            binding.firstProgress.visibility = View.GONE
            binding.firstComplete.visibility = View.VISIBLE
        }
        if (sharedPreference.getBoolean(Constant.PERSONAL_DETAILS, false)) {
            binding.secondTextView.visibility = View.GONE
            binding.secondProgress.visibility = View.GONE
            binding.secondComplete.visibility = View.VISIBLE
        }
        if (sharedPreference.getBoolean(Constant.ADDRESS_VERIFICATION, false)) {
            binding.thirdTextView.visibility = View.GONE
            binding.thirdProgress.visibility = View.GONE
            binding.thirdComplete.visibility = View.VISIBLE
        }
        if (sharedPreference.getBoolean(Constant.KYC_DETAILS, false)) {
            binding.fourthTextView.visibility = View.GONE
            binding.fourthProgress.visibility = View.GONE
            binding.fourthComplete.visibility = View.VISIBLE
        }
        if (sharedPreference.getBoolean(Constant.EMPLOYMENT_DETAILS, false)) {
            binding.fifthTextView.visibility = View.GONE
            binding.fifthProgress.visibility = View.GONE
            binding.fifthComplete.visibility = View.VISIBLE
        }
        when (fragmentName) {
            Constant.LOAN_DETAILS -> {
                binding.firstTextView.setTextColor(ContextCompat.getColor(this, R.color.white))
                binding.firstProgress.setImageResource(R.drawable.circle_filled)
                binding.secondTextView.setTextColor(ContextCompat.getColor(this, R.color.primary))
                binding.secondProgress.setImageResource(R.drawable.circle)
                binding.thirdTextView.setTextColor(ContextCompat.getColor(this, R.color.primary))
                binding.thirdProgress.setImageResource(R.drawable.circle)
                binding.fourthTextView.setTextColor(ContextCompat.getColor(this, R.color.primary))
                binding.fourthProgress.setImageResource(R.drawable.circle)
                binding.fifthTextView.setTextColor(ContextCompat.getColor(this, R.color.primary))
                binding.fifthProgress.setImageResource(R.drawable.circle)
            }
            Constant.PERSONAL_DETAILS -> {
                binding.secondTextView.setTextColor(ContextCompat.getColor(this, R.color.white))
                binding.secondProgress.setImageResource(R.drawable.circle_filled)
                binding.firstTextView.setTextColor(ContextCompat.getColor(this, R.color.primary))
                binding.firstProgress.setImageResource(R.drawable.circle)
                binding.thirdTextView.setTextColor(ContextCompat.getColor(this, R.color.primary))
                binding.thirdProgress.setImageResource(R.drawable.circle)
                binding.fourthTextView.setTextColor(ContextCompat.getColor(this, R.color.primary))
                binding.fourthProgress.setImageResource(R.drawable.circle)
                binding.fifthTextView.setTextColor(ContextCompat.getColor(this, R.color.primary))
                binding.fifthProgress.setImageResource(R.drawable.circle)
            }
            Constant.ADDRESS_VERIFICATION -> {
                binding.thirdTextView.setTextColor(ContextCompat.getColor(this, R.color.white))
                binding.thirdProgress.setImageResource(R.drawable.circle_filled)
                binding.firstTextView.setTextColor(ContextCompat.getColor(this, R.color.primary))
                binding.firstProgress.setImageResource(R.drawable.circle)
                binding.secondTextView.setTextColor(ContextCompat.getColor(this, R.color.primary))
                binding.secondProgress.setImageResource(R.drawable.circle)
                binding.fourthTextView.setTextColor(ContextCompat.getColor(this, R.color.primary))
                binding.fourthProgress.setImageResource(R.drawable.circle)
                binding.fifthTextView.setTextColor(ContextCompat.getColor(this, R.color.primary))
                binding.fifthProgress.setImageResource(R.drawable.circle)
            }
            Constant.KYC_DETAILS -> {
                binding.fourthTextView.setTextColor(ContextCompat.getColor(this, R.color.white))
                binding.fourthProgress.setImageResource(R.drawable.circle_filled)
                binding.firstTextView.setTextColor(ContextCompat.getColor(this, R.color.primary))
                binding.firstProgress.setImageResource(R.drawable.circle)
                binding.secondTextView.setTextColor(ContextCompat.getColor(this, R.color.primary))
                binding.secondProgress.setImageResource(R.drawable.circle)
                binding.thirdTextView.setTextColor(ContextCompat.getColor(this, R.color.primary))
                binding.thirdProgress.setImageResource(R.drawable.circle)
                binding.fifthTextView.setTextColor(ContextCompat.getColor(this, R.color.primary))
                binding.fifthProgress.setImageResource(R.drawable.circle)
                binding.thirdTextView.setTextColor(ContextCompat.getColor(this, R.color.primary))
                binding.thirdProgress.setImageResource(R.drawable.circle)
            }
            Constant.EMPLOYMENT_DETAILS -> {
                binding.fifthTextView.setTextColor(ContextCompat.getColor(this, R.color.white))
                binding.fifthProgress.setImageResource(R.drawable.circle_filled)
                binding.firstTextView.setTextColor(ContextCompat.getColor(this, R.color.primary))
                binding.firstProgress.setImageResource(R.drawable.circle)
                binding.secondTextView.setTextColor(ContextCompat.getColor(this, R.color.primary))
                binding.secondProgress.setImageResource(R.drawable.circle)
                binding.thirdTextView.setTextColor(ContextCompat.getColor(this, R.color.primary))
                binding.thirdProgress.setImageResource(R.drawable.circle)
                binding.fourthTextView.setTextColor(ContextCompat.getColor(this, R.color.primary))
                binding.fourthProgress.setImageResource(R.drawable.circle)
            }
        }
    }

    fun showProgressBar() {
        binding.progressBarCL.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        binding.progressBarCL.visibility = View.GONE
    }
}