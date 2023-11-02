package com.example.duestake.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.duestake.data.emptra.CompanyData
import com.example.duestake.databinding.SpinnerListBinding
import com.example.duestake.ui.viewmodels.OnboardingViewModel

class CompanyAdapter(private val viewModel: OnboardingViewModel) : RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder>() {

    inner class CompanyViewHolder(val binding: SpinnerListBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<CompanyData>() {
        override fun areItemsTheSame(oldItem: CompanyData, newItem: CompanyData): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: CompanyData, newItem: CompanyData): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyViewHolder {
        return CompanyViewHolder(
            SpinnerListBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: CompanyViewHolder, position: Int) {
        val companyList = differ.currentList[position]
        holder.binding.apply {
            name.text = companyList.name
            name.setOnClickListener {
                viewModel.companyName.postValue(companyList.name)
            }
        }
    }
}