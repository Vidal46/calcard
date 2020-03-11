package br.com.calcard.android.app.utils.textwatcher

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import br.com.calcard.android.app.utils.CurrencyHelper
import br.com.calcard.android.app.utils.StringUtil
import java.lang.ref.WeakReference

class CurrencyFormatWatcher(editText: EditText) : TextWatcher {
    private val editTextWeakReference: WeakReference<EditText> = WeakReference(editText)
    private var current = ""
    private var firstValue = ""
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (s.toString() != current) {
            if (current.isNotEmpty() && s.toString() != firstValue) {
                val cleanString: String = StringUtil.unmask(s.toString())
                current = CurrencyHelper.format(cleanString.toDouble() / 100)
            } else {
                current = CurrencyHelper.format(s.toString().toDouble())
                firstValue = s.toString()
            }
            val editText = editTextWeakReference.get()
            editText!!.removeTextChangedListener(this)
            editText.setText(current)
            editText.setSelection(current.length)
            editText.addTextChangedListener(this)
        }
    }

    override fun afterTextChanged(editable: Editable) {}

}