package com.economiaon.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.economiaon.R
import com.economiaon.data.UserPreferences
import com.economiaon.data.model.Finance
import com.economiaon.data.model.FinanceType
import com.economiaon.data.model.User
import com.economiaon.databinding.ActivityAddFinanceBinding
import com.economiaon.ui.navigation.financelist.FinancesListFragment
import com.economiaon.util.text
import com.economiaon.viewmodel.AddFinanceViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AddFinanceActivity : AppCompatActivity() {
    private val _binding by lazy { ActivityAddFinanceBinding.inflate(layoutInflater) }
    private val viewModel by viewModel<AddFinanceViewModel>()
    private var userId: Long = 0

    init {
        val userPrefs = UserPreferences(applicationContext)
        lifecycleScope.launch {
            userPrefs.userId.collect { id ->
                userId = id ?: 0
            }
        }
    }

    companion object Extras {
        const val FINANCE = "EXTRA_FINANCE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(_binding.root)
        setSupportActionBar(_binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = applicationContext.getString(R.string.cd_fab_add_finance)

        setupUi()
    }

    override fun onResume() {
        super.onResume()
        setupDropDown()
    }

    private fun setupUi() {
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
                if (tilFinanceStart.text.isBlank()) {
                    tilFinanceStart.error = resources.getString(R.string.txt_finance_start_error)
                    tilFinanceStart.requestFocus()
                    return@setOnClickListener
                }
                if (tilFinanceEnd.text.isBlank()) {
                    tilFinanceEnd.error = resources.getString(R.string.txt_finance_end_error)
                    tilFinanceEnd.requestFocus()
                    return@setOnClickListener
                }
                if (tilFinancePrice.text.isBlank()) {
                    tilFinancePrice.error = resources.getString(R.string.txt_finance_price_error)
                    tilFinancePrice.requestFocus()
                    return@setOnClickListener
                }
                val pattern = "yyyy-MM-dd"
                viewModel.saveFinance(
                    Finance(
                        name = tilFinanceName.text,
                        type = toFinanceTypeEnum(tilFinanceType.text),
                        financePrice = tilFinancePrice.text.toBigDecimal(),
                        initialDate = LocalDate.parse(
                            tilFinanceStart.text,
                            DateTimeFormatter.ofPattern(pattern)
                        ),
                        finalDate = LocalDate.parse(
                            tilFinanceEnd.text,
                            DateTimeFormatter.ofPattern(pattern)
                        ),
                        user = viewModel.loggedUser.value as User
                    )
                )
            }
        }

        intent?.extras.let {
            val finance = it?.getParcelable<Finance>(FINANCE)
            if (finance != null) {
                _binding.apply {
                    tilFinanceName.text = finance.name
                    val pattern = "yyyy-MM-dd"
                    tilFinanceStart.text =
                        finance.initialDate.format(DateTimeFormatter.ofPattern(pattern))
                    tilFinanceEnd.text =
                        finance.finalDate.format(DateTimeFormatter.ofPattern(pattern))
                    tilFinanceType.text = defineFinanceType(finance)

                    tilFinancePrice.text = finance.financePrice.toString()
                    btnCreateFin.text = resources.getString(R.string.txt_edit_finance)

                    btnCreateFin.setOnClickListener {
                        viewModel.getUserById(userId)
                        if (viewModel.loggedUser.value == null) return@setOnClickListener
                        val newFinance = Finance(
                            id = userId,
                            name = tilFinanceName.text,
                            type = toFinanceTypeEnum(tilFinanceType.text),
                            financePrice = tilFinancePrice.text.toBigDecimal(),
                            initialDate = LocalDate.parse(tilFinanceStart.text,
                                DateTimeFormatter.ofPattern(pattern)),
                            finalDate = LocalDate.parse(tilFinanceEnd.text,
                                DateTimeFormatter.ofPattern(pattern)),
                            user = viewModel.loggedUser.value as User)
                        viewModel.updateFinance(newFinance)
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
                Toast.makeText(this, R.string.txt_finance_updated, Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                supportFragmentManager.beginTransaction()
                    .add(R.id.navigation_finance_list, FinancesListFragment.newInstance())
                    .commit()
            } else {
                Toast.makeText(this, R.string.txt_unexpected_error, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.isFinanceCreated.observe(this) {
            if (it) {
                Toast.makeText(this, R.string.txt_finance_created, Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                supportFragmentManager.beginTransaction()
                    .add(R.id.navigation_finance_list, FinancesListFragment.newInstance())
                    .commit()
            } else {
                Toast.makeText(this, R.string.txt_unexpected_error, Toast.LENGTH_SHORT).show()
            }
        }
    }
}