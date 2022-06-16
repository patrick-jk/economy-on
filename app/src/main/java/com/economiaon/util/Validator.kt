package com.economiaon.util

import android.content.Context
import android.text.InputType
import androidx.core.content.ContextCompat
import com.economiaon.R
import com.google.android.material.textfield.TextInputLayout

object Validator {
    private fun validateEmail(email: String): Boolean {
        return !(email.contains("@"))
    }

    private fun validateField(name: String): Boolean {
        return !(name.isEmpty() || name.isBlank())
    }

    fun validateEmailAndConfirmation(email: String, emailConfirmation: String): Boolean {
        return (email == emailConfirmation)
    }

    fun validatePasswordAndConfirmation(password: String, passwordConfirmation: String): Boolean {
        return (password == passwordConfirmation)
    }

    fun validateFieldAndSetErrorWhenInvalid(field: TextInputLayout, stringRes: Int, context: Context) {
        var emailValidation = false
        if (field.editText?.inputType == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS) {
            emailValidation = validateEmail(field.text)
        }
        if (!validateField(field.text) || emailValidation) {
            field.error = context.getString(stringRes)
            field.requestFocus()
        } else {
           field.isErrorEnabled = false
        }
    }
}