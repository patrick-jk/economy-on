package com.economiaon.util

import android.content.Context
import com.google.android.material.textfield.TextInputLayout

object Validator {
    private fun validateEmail(email: String): Boolean {
        return (email.isEmpty() || email.isBlank() || !email.contains("@"))
    }

    private fun validateField(name: String): Boolean {
        return (name.isEmpty() || name.isBlank())
    }

    fun validateEmailInfo(email: TextInputLayout, stringRes: Int, context: Context): Boolean {
        return validateGenericFields(email, context, stringRes, validateEmail(email.text))
    }

    fun validateField(field: TextInputLayout, stringRes: Int, context: Context): Boolean {
        return validateGenericFields(field, context, stringRes, validateField(field.text))
    }

    private fun validateGenericFields(
        field: TextInputLayout, context: Context, stringRes: Int, condition: Boolean
    ): Boolean {
        return if (condition) {
            field.error = context.getString(stringRes)
            field.requestFocus()
            true
        } else {
            field.isErrorEnabled = false
            false
        }
    }
}