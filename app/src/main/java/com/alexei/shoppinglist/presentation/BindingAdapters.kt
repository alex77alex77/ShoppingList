package com.alexei.shoppinglist.presentation

import androidx.databinding.BindingAdapter
import com.alexei.shoppinglist.R
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("errorInputName")
fun bindErrorInputName(inputName: TextInputLayout, isError: Boolean) {
    val message = if (isError) {
        inputName.context.getString(R.string.err_input_name)
    } else {
        null
    }
    inputName.error = message
}

@BindingAdapter("errorInputCount")
fun bindErrorInputCount(inputCount: TextInputLayout, isError: Boolean) {
    val message = if (isError) {
        inputCount.context.getString(R.string.err_input_name)
    } else {
        null
    }
    inputCount.error = message
}

