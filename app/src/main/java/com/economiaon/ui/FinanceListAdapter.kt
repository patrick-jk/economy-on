package com.economiaon.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.economiaon.R
import com.economiaon.databinding.ItemFinancesBinding
import com.economiaon.domain.Finance
import com.economiaon.domain.FinanceType
import java.time.LocalDate
import java.time.Period
import java.util.*

class FinanceListAdapter : ListAdapter<Finance, FinanceListAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFinancesBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemFinancesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val context: Context = binding.root.context
        fun bind(item: Finance) {
            binding.tvFinName.text = item.name
            binding.tvFinType.text = when (item.type) {
                FinanceType.PHYSIOLOGY -> context.getString(R.string.finance_type_physiology)
                FinanceType.SECURITY -> context.getString(R.string.finance_type_security)
                FinanceType.LEISURE -> context.getString(R.string.finance_type_leisure)
                FinanceType.SOCIAL -> context.getString(R.string.finance_type_social)
                FinanceType.PERSONAL_DEVELOPMENT -> context.getString(R.string.finance_type_personal_development)
            }
            val installmentsDuration = Period.between(item.initialDate, LocalDate.now()).months
            val finalMonth = Period.between(item.initialDate, item.finalDate).months
            binding.tvFinPeriod.text = context.getString(R.string.finance_time, installmentsDuration, finalMonth)
            binding.tvFinPrice.text = String.format(Locale.getDefault(), "$%s", item.financePrice)

        }

    }
}

class DiffCallback : DiffUtil.ItemCallback<Finance>() {
    override fun areItemsTheSame(oldItem: Finance, newItem: Finance) = oldItem == newItem
    override fun areContentsTheSame(oldItem: Finance, newItem: Finance) = oldItem.id == newItem.id
}