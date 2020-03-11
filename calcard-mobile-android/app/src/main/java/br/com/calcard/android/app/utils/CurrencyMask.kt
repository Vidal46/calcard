package br.com.calcard.android.app.utils

import android.text.TextWatcher
import android.widget.EditText
import br.com.calcard.android.app.utils.textwatcher.CurrencyFormatWatcher
import java.math.BigDecimal

object CurrencyMask {

    fun insert(mask: String?, editText: EditText?): TextWatcher? {
        return editText?.let { CurrencyFormatWatcher(it) }
    }

    fun apply(`val`: BigDecimal?): String {
        return StringUtil.decimalFormat.format(`val`)
    }
}