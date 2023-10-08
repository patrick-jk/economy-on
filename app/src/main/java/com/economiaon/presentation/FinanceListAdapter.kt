package com.economiaon.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.economiaon.R
import com.economiaon.databinding.ItemFinancesBinding
import com.economiaon.domain.model.Finance
import com.economiaon.domain.model.FinanceType
import java.text.NumberFormat
import java.time.LocalDate
import java.time.Period
import java.util.Locale

class FinanceListAdapter(private val onClick: (Finance) -> Unit) : ListAdapter<Finance, FinanceListAdapter.ViewHolder>(DiffCallback()) {

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
        fun bind(item: Finance) {
            val context = itemView.context

            binding.apply {
                tvFinName.text = item.name
                tvFinType.text = when (item.type) {
                    FinanceType.PHYSIOLOGY -> context.getString(R.string.finance_type_physiology)
                    FinanceType.SECURITY -> context.getString(R.string.finance_type_security)
                    FinanceType.LEISURE -> context.getString(R.string.finance_type_leisure)
                    FinanceType.SOCIAL -> context.getString(R.string.finance_type_social)
                    FinanceType.PERSONAL_DEVELOPMENT -> context.getString(R.string.finance_type_personal_development)
                }
                val installmentStart: LocalDate = LocalDate.parse(item.initialDate)
                val installmentEnd: LocalDate = LocalDate.parse(item.finalDate)
                val initialInstallmentPeriod = if (installmentEnd > LocalDate.now()) {
                    Period.between(installmentStart, LocalDate.now())
                } else {
                    Period.between(installmentStart, installmentEnd)
                }
                val finalInstallmentPeriod = Period.between(installmentStart, installmentEnd)

                val initialInstallment = if (initialInstallmentPeriod.years == 0) {
                    initialInstallmentPeriod.months
                } else {
                    initialInstallmentPeriod.years * 12 + initialInstallmentPeriod.months
                }

                if (finalInstallmentPeriod.years == 0) {
                    val finalInstallment = finalInstallmentPeriod.months
                    tvFinPeriod.text = context.getString(R.string.finance_time, initialInstallment, finalInstallment)
                } else {
                    val remainingYearsInMonths = finalInstallmentPeriod.years * 12
                    val remainingMonths = finalInstallmentPeriod.months
                    tvFinPeriod.text = context.getString(R.string.finance_time, initialInstallment, remainingYearsInMonths + remainingMonths)
                }
                tvFinPrice.text = NumberFormat.getCurrencyInstance(Locale.getDefault()).format(item.financeCost)

                btnEditFin.setOnClickListener {
                    onClick(item)
                }
            }
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<Finance>() {
    override fun areItemsTheSame(oldItem: Finance, newItem: Finance) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Finance, newItem: Finance) = oldItem == newItem
}