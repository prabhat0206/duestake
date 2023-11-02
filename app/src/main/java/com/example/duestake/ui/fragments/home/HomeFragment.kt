package com.example.duestake.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.duestake.adapter.BankAdapter
import com.example.duestake.data.Result
import com.example.duestake.databinding.FragmentHomeBinding
import com.example.duestake.ui.fragments.BaseFragment
import com.example.duestake.ui.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var adapter: BankAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        init()

        return binding.root
    }

    private fun init() {
        CoroutineScope(Dispatchers.IO).launch {
            animate()
        }

        adapter = BankAdapter()

        binding.rvBankList.adapter = adapter
    }

    private suspend fun animate() {
        withContext(Dispatchers.Main) {
            binding.imageView.animate().alpha(1f).duration = 1500
        }
        delay(1000)
        withContext(Dispatchers.Main) {
            binding.congratulationsTextView.animate().alpha(1f).duration = 700
        }
        delay(1000)
        withContext(Dispatchers.Main) {
            binding.congratulationsSubTextView.animate().alpha(1f).duration = 700
        }
        delay(1000)

        withContext(Dispatchers.Main) {
            fetchBankList()
        }
    }

    private fun fetchBankList() {
        viewModel.bankList()

        viewModel.bankListResource.observe( viewLifecycleOwner ) { response ->
            when (response) {
                is Result.Success -> {
                    if (response.data.success) {
                        hideProgressBar(binding.progressBarCL)
                        hideErrorMessage(binding.errorMessageCL)
                        adapter.differ.submitList(response.data.banks)
                    }
//                    else {
//                        hideProgressBar(binding.progressBarCL)
//                        hideErrorMessage(binding.errorMessageCL)
//                        Toast.makeText(
//                            activity, "An error occurred: ${response.data.banks}", Toast.LENGTH_LONG
//                        ).show()
//                    }
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
    }
}