package br.com.calcard.android.app.utils

import android.widget.EditText
import androidx.databinding.BindingAdapter

object BindAdapters {
    @BindingAdapter("bind:mask")
    @JvmStatic
    fun setMask(editText: EditText, mask: String?) {
        editText.addTextChangedListener(CurrencyMask.insert(mask, editText))
    }
}