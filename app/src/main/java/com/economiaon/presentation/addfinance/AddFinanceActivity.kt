package com.economiaon.presentation.addfinance

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.economiaon.R
import com.economiaon.data.datastore.UserPreferences
import com.economiaon.databinding.ActivityAddFinanceBinding
import com.economiaon.domain.model.Finance
import com.economiaon.domain.model.FinanceType
import com.economiaon.presentation.MainActivity
import com.economiaon.util.DatePickerFragment
import com.economiaon.util.createDialog
import com.economiaon.util.text
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.UUID

class AddFinanceActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private val _binding by lazy { ActivityAddFinanceBinding.inflate(layoutInflater) }
    private val viewModel by viewModel<AddFinanceViewModel>()
    private var userId: String = ""
    private val userPrefs by lazy { UserPreferences(this) }

    private val finance by lazy {
        intent?.extras?.getParcelable<Finance>(FINANCE)
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
        if (intent.hasExtra(FINANCE)) {
            intent?.extras.let {
                finance?.let { fin ->
                    _binding.apply {
                        tilFinanceName.text = finance!!.name
                        val pattern = SimpleDateFormat("yyyy-MM-dd")
                        val inputPattern = SimpleDateFormat("dd/MM/yyyy")
                        tilFinanceStart.text = inputPattern.format(pattern.parse(finance!!.initialDate)!!)
                        tilFinanceEnd.text = inputPattern.format(pattern.parse(finance!!.finalDate)!!)
                        tilFinanceType.text = defineFinanceType(finance!!)
                        tilFinancePrice.text = finance!!.financeCost.toString()
                        btnCreateFin.text = resources.getString(R.string.txt_edit_finance)
                        btnDeleteFin.visibility = View.VISIBLE

                        btnDeleteFin.setOnClickListener {
                            createDialog {
                                setMessage(resources.getString(R.string.txt_delete_finance))
                                    .setPositiveButton(android.R.string.ok) { _, _ ->
                                        viewModel.deleteFinance(fin.id)
                                    }
                                    .setNegativeButton(android.R.string.cancel, null)
                            }.show()
                        }
                    }
                }
            }
        }

        if (intent.hasExtra(FINANCE)) {
            _binding.tilFinanceStart.editText?.setOnClickListener {
                Log.i("AddFinanceActivity", "setupUi: ${finance!!.initialDate}")
                showDatePickerDialog("Start", LocalDate.parse(finance!!.initialDate))
            }

            _binding.tilFinanceEnd.editText?.setOnClickListener {
                Log.i("AddFinanceActivity", "setupUi: ${finance!!.finalDate}")
                showDatePickerDialog("End", LocalDate.parse(finance!!.finalDate))
            }
        } else {
            _binding.tilFinanceStart.editText?.setOnClickListener {
                showDatePickerDialog("Start")
            }

            _binding.tilFinanceEnd.editText?.setOnClickListener {
                showDatePickerDialog("End")
                DatePickerFragment.newInstance(this@AddFinanceActivity)
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

                val finance = createFinance()
                if (btnCreateFin.text == resources.getString(R.string.cd_fab_add_finance)) viewModel.saveFinance(finance)
                else {
                    createDialog {
                        setMessage(resources.getString(R.string.txt_update_finance))
                            .setPositiveButton(android.R.string.ok) { _, _ ->
                                viewModel.updateFinance(finance)
                            }
                            .setNegativeButton(android.R.string.cancel, null)
                    }.show()

                }
            }
        }
    }

    private fun showDatePickerDialog(tag: String, actualDate: LocalDate = LocalDate.now()) {
        Log.i("AddFinanceActivity", "showDatePickerDialog: $actualDate")
        val datePicker = DatePickerFragment.newInstance(this, actualDate)
        datePicker.show(supportFragmentManager, "datePicker$tag")
    }

    @SuppressLint("SimpleDateFormat")
    private fun createFinance(): Finance {
        _binding.apply {
            val inputPattern = SimpleDateFormat("dd/MM/yyyy")
            val pattern = SimpleDateFormat("yyyy-MM-dd")

            return Finance(
                id = this@AddFinanceActivity.finance?.id ?: UUID.randomUUID().toString(),
                name = tilFinanceName.text,
                type = toFinanceTypeEnum(tilFinanceType.text),
                financeCost = tilFinancePrice.text.toDouble(),
                initialDate = Date.valueOf(pattern.format(inputPattern.parse(tilFinanceStart.text)!!)).toString(),
                finalDate = Date.valueOf(pattern.format(inputPattern.parse(tilFinanceEnd.text)!!)).toString(),
                userEmail = userId
            )
        }
    }

    private fun setupDropDown() {
        val financeTypes = resources.getStringArray(R.array.finance_types)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, financeTypes)
        _binding.actvFinanceType.setAdapter(arrayAdapter)
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
                financeNotification(R.string.txt_finance_updated, R.string.txt_finance_deleted_connect_to_internet)

            }
        }
        viewModel.isFinanceCreated.observe(this) {
            if (it) {
                financeNotification(R.string.txt_finance_created)
            } else {
                financeNotification(R.string.txt_finance_created, R.string.txt_finance_deleted_connect_to_internet)

            }
        }
        viewModel.isFinanceDeleted.observe(this) {
            if (it) {
                financeNotification(R.string.txt_finance_deleted)
            } else {
                financeNotification(R.string.txt_finance_deleted, R.string.txt_finance_deleted_connect_to_internet)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    private fun financeNotification(stringRes: Int, stringRes2: Int? = null) {
        createDialog {
            setMessage((resources.getString(stringRes) + resources.getString(stringRes2 ?: R.string.txt_empty)))
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    startActivity(Intent(this@AddFinanceActivity, MainActivity::class.java))
                }
        }.show()
    }

    override fun onDateSet(date: DatePicker?, year: Int, month: Int, day: Int) {
        val monthString = if (month < 9) "0${month + 1}" else "${month + 1}"
        val dayString = if (day < 10) "0$day" else "$day"
        val selectedDate = "$dayString/$monthString/$year"

        if (_binding.tilFinanceStart.editText?.hasFocus() == true) {
            _binding.tilFinanceStart.text = selectedDate
        } else {
            _binding.tilFinanceEnd.text = selectedDate
        }
    }
}