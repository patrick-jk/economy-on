package com.economiaon.ui

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.economiaon.R
import com.economiaon.data.connection.service.ApiService
import com.economiaon.data.model.Finance
import com.economiaon.data.model.FinanceType
import com.economiaon.databinding.ItemFinancesBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection
import java.time.LocalDate
import java.time.Period
import java.util.*

class FinanceListAdapter : ListAdapter<Finance, FinanceListAdapter.ViewHolder>(DiffCallback()) {
    private val _isDeleted = MutableLiveData<Boolean>()
    val isDeleted: LiveData<Boolean> = _isDeleted

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFinancesBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), holder.itemView.context)
    }

    inner class ViewHolder(private val binding: ItemFinancesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Finance, context: Context) {
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
                val initialInstallmentPeriod = Period.between(installmentStart, LocalDate.now())
                val finalInstallmentPeriod = Period.between(installmentStart, installmentEnd)

                val initialInstallment =  if (initialInstallmentPeriod.years == 0) {
                    initialInstallmentPeriod.months
                } else {
                    initialInstallmentPeriod.years * 12 + initialInstallmentPeriod.months
                }

                if (finalInstallmentPeriod.years == 0) {
                    val finalInstallment = finalInstallmentPeriod.months
                    tvFinPeriod.text =
                        context.getString(R.string.finance_time, initialInstallment + 1,
                            finalInstallment)
                } else {
                    val remainingYearsInMonths = finalInstallmentPeriod.years * 12
                    val remainingMonths = finalInstallmentPeriod.months
                    tvFinPeriod.text =
                        context.getString(R.string.finance_time, initialInstallment + 1,
                            remainingYearsInMonths + remainingMonths)
                }
                tvFinPrice.text = String.format(Locale.getDefault(), "$%.2f", item.financePrice)
                btnEditFin.setOnClickListener {
                    val intent = Intent(context, AddFinanceActivity::class.java)
                    intent.putExtra(AddFinanceActivity.FINANCE, item)
                    context.startActivity(intent)
                }
                btnDeleteFin.setOnClickListener {
                    val dialog = AlertDialog.Builder(context)
                        .setTitle(R.string.txt_delete_finance)
                        .setMessage(R.string.txt_warning_delete_finance)
                        .setPositiveButton(android.R.string.ok) { _, _ ->
                            val deletedFinance = ApiService.getInstance().deleteFinance(item.id)
                            deletedFinance.enqueue(object : Callback<Void> {
                                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                    if (response.code() == HttpURLConnection.HTTP_NO_CONTENT) {
                                        _isDeleted.postValue(true)
                                        Toast.makeText(
                                            context, R.string.txt_finance_deleted,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        _isDeleted.postValue(false)
                                        Toast.makeText(
                                            context, R.string.txt_unexpected_error,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                                override fun onFailure(call: Call<Void>, t: Throwable) {
                                    Toast.makeText(
                                        context, R.string.txt_unexpected_error,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            })
                        }.setNegativeButton(android.R.string.cancel) { dialog, _ ->
                            dialog.dismiss()
                        }
                    dialog.show()
                }
            }
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<Finance>() {
    override fun areItemsTheSame(oldItem: Finance, newItem: Finance) = oldItem == newItem
    override fun areContentsTheSame(oldItem: Finance, newItem: Finance) = oldItem.id == newItem.id
}