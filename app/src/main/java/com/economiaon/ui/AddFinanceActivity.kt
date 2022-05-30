package com.economiaon.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.economiaon.R
import com.economiaon.databinding.ActivityAddFinanceBinding

class AddFinanceActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAddFinanceBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupClicks()
    }

    override fun onResume() {
        super.onResume()
        setupDropDown()
    }

    private fun setupClicks() {

    }

    private fun setupDropDown() {
        val financeTypes = resources.getStringArray(R.array.finance_types)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, financeTypes)
        binding.actvFinanceType.setAdapter(arrayAdapter)
    }
}