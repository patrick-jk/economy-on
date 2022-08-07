package com.economiaon.presentation.addfinance

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.economiaon.R
import com.economiaon.data.UserPreferences
import com.economiaon.databinding.ActivityAddFinanceBinding
import com.economiaon.domain.model.Finance
import com.economiaon.domain.model.FinanceType
import com.economiaon.presentation.MainActivity
import com.economiaon.presentation.statepattern.UserState
import com.economiaon.util.createDialog
import com.economiaon.util.text
import com.santalu.maskara.Mask
import com.santalu.maskara.MaskChangedListener
import com.santalu.maskara.MaskStyle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.sql.Date
import java.text.SimpleDateFormat

class AddFinanceActivity : AppCompatActivity() {
    private val _binding by lazy { ActivityAddFinanceBinding.inflate(layoutInflater) }
    private val viewModel by viewModel<AddFinanceViewModel>()
    private var userId: Long = 0
    private val userPrefs by lazy { UserPreferences(this) }

    companion object Extras {
        const val FINANCE = "EXTRA_FINANCE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(_binding.root)
        setSupportActionBar(_binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = applicationContext.getString(R.string.cd_fab_add_finance)
        lifecycleScope.launch {
            userPrefs.userId.collect { id ->
                id?.let {
                    userId = it
                }
            }
        }

        setupUi()
    }

    override fun onResume() {
        super.onResume()
        setupDropDown()
    }

    @SuppressLint("SimpleDateFormat")
    private fun setupUi() {
        val dateMask = Mask(value = "__/__/____", character = '_', style = MaskStyle.PERSISTENT)

        val initialDateListener = MaskChangedListener(dateMask)
        _binding.tilFinanceStart.editText?.addTextChangedListener(initialDateListener)

        val finalDateListener = MaskChangedListener(dateMask)
        _binding.tilFinanceEnd.editText?.addTextChangedListener(finalDateListener)

        if (intent.hasExtra(FINANCE)) {
            intent?.extras.let {
                val finance = it?.getParcelable<Finance>(FINANCE)
                finance?.let {
                    _binding.apply {
                        tilFinanceName.text = finance.name
                        val pattern = SimpleDateFormat("yyyy-MM-dd")
                        val inputPattern = SimpleDateFormat("dd/MM/yyyy")
                        tilFinanceStart.text = inputPattern.format(pattern.parse(finance.initialDate)!!)
                        tilFinanceEnd.text = inputPattern.format(pattern.parse(finance.finalDate)!!)
                        tilFinanceType.text = defineFinanceType(finance)
                        tilFinancePrice.text = finance.financePrice.toString()
                        btnCreateFin.text = resources.getString(R.string.txt_edit_finance)
                        btnDeleteFin.visibility = View.VISIBLE
                        btnDeleteFin.setOnClickListener {
                            viewModel.deleteFinance(finance.id)
                        }
                    }
                }
            }
        }
        _binding.apply {
            btnCreateFin.setOnClickListener {
                if (tilFinanceName.text.isBlank()) {
                    tilFinanceName.error = resources.getString(R.string.txt_finance_name_error)
                    tilFinanceName.requestFocus()
                    return@setOnClickListener
                }
                if (tilFinanceType.text.isBlank()) {
                    tilFinanceType.error = resources.getString(R.string.txt_finance_type_error)
                    tilFinanceType.requestFocus()
                    return@setOnClickListener
                }
                if (initialDateListener.unMasked.isBlank()) {
                    tilFinanceStart.error = resources.getString(R.string.txt_finance_start_error)
                    tilFinanceStart.requestFocus()
                    return@setOnClickListener
                }
                if (finalDateListener.unMasked.isBlank()) {
                    tilFinanceEnd.error = resources.getString(R.string.txt_finance_end_error)
                    tilFinanceEnd.requestFocus()
                    return@setOnClickListener
                }
                if (tilFinancePrice.text.isBlank()) {
                    tilFinancePrice.error = resources.getString(R.string.txt_finance_price_error)
                    tilFinancePrice.requestFocus()
                    return@setOnClickListener
                }
                viewModel.getUserById(userId)

                viewModel.loggedUser.observe(this@AddFinanceActivity) {
                    when (it) {
                        is UserState.Loading -> {}
                        is UserState.Error -> {
                            createDialog {
                                setMessage(it.error.message)
                            }
                        }
                        is UserState.Success -> {
                            val inputPattern = SimpleDateFormat("dd/MM/yyyy")
                            val pattern = SimpleDateFormat("yyyy-MM-dd")
                            val finance = Finance(
                                id = 0, name = tilFinanceName.text,
                                type = toFinanceTypeEnum(tilFinanceType.text),
                                financePrice = tilFinancePrice.text.toBigDecimal(),
                                initialDate = Date.valueOf(pattern.format(inputPattern.parse(initialDateListener.masked)!!)).toString(),
                                finalDate = Date.valueOf(pattern.format(inputPattern.parse(finalDateListener.masked)!!)).toString(),
                                user = it.user
                            )
                            if (btnCreateFin.text == resources.getString(R.string.cd_fab_add_finance))
                                viewModel.saveFinance(finance)
                            else
                                viewModel.updateFinance(finance)
                        }
                    }
                }
            }
        }
    }

    private fun setupDropDown() {
        val financeTypes = resources.getStringArray(R.array.finance_types)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, financeTypes)
        _binding.actvFinanceType.setAdapter(arrayAdapter)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun defineFinanceType(finance: Finance): String {
        return when (finance.type) {
            FinanceType.PHYSIOLOGY -> this.getString(R.string.finance_type_physiology)
            FinanceType.SECURITY -> this.getString(R.string.finance_type_security)
            FinanceType.LEISURE -> this.getString(R.string.finance_type_leisure)
            FinanceType.SOCIAL -> this.getString(R.string.finance_type_social)
            FinanceType.PERSONAL_DEVELOPMENT -> this.getString(R.string.finance_type_personal_development)
        }
    }

    private fun toFinanceTypeEnum(string: String): FinanceType {
        return when (string) {
            this.getString(R.string.finance_type_physiology) -> FinanceType.PHYSIOLOGY
            this.getString(R.string.finance_type_security) -> FinanceType.SECURITY
            this.getString(R.string.finance_type_leisure) -> FinanceType.LEISURE
            this.getString(R.string.finance_type_social) -> FinanceType.SOCIAL
            this.getString(R.string.finance_type_personal_development) -> FinanceType.PERSONAL_DEVELOPMENT
            else -> FinanceType.SOCIAL
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.isFinanceUpdated.observe(this) {
            if (it) {
                financeNotification(R.string.txt_finance_updated)
            } else {
                errorToast()
            }
        }
        viewModel.isFinanceCreated.observe(this) {
            if (it) {
                financeNotification(R.string.txt_finance_created)
            } else {
                errorToast()
            }
        }
        viewModel.isFinanceDeleted.observe(this) {
            if (it) {
                financeNotification(R.string.txt_finance_deleted)
            } else {
                errorToast()
            }
        }
    }

    private fun errorToast() =
        Toast.makeText(this, resources.getString(R.string.txt_unexpected_error), Toast.LENGTH_SHORT).show()

    private fun financeNotification(stringRes: Int) {
        Toast.makeText(this, stringRes, Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity::class.java))
    }
}