package com.example.duestake.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.duestake.data.bank.BankList
import com.example.duestake.databinding.BankItemViewBinding

class BankAdapter : RecyclerView.Adapter<BankAdapter.BankViewHolder>() {

    inner class BankViewHolder(val binding: BankItemViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<BankList>() {
        override fun areItemsTheSame(oldItem: BankList, newItem: BankList): Boolean {
            return oldItem.Title == newItem.Title
        }

        override fun areContentsTheSame(oldItem: BankList, newItem: BankList): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankViewHolder {
        return BankViewHolder(
            BankItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BankViewHolder, position: Int) {
        val bankList = differ.currentList[position]
        holder.binding.apply {
            Glide.with(view).load(bankList.BankLogo).into(bankImageView)
            bankName.text = bankList.Title
            offerAmount.text = bankList.Description
        }
    }
}