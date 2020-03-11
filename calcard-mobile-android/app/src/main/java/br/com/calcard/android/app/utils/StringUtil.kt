package br.com.calcard.android.app.utils

import java.text.DecimalFormat

object StringUtil {
    const val EMPTY = ""
    val decimalFormat = DecimalFormat("R$ #,##0.00")

    fun unmask(numeric: String?): String {
        if (numeric != null) {
            val numbers = numeric.replace("[^\\d]".toRegex(), EMPTY)
            if (numeric.contains(",") && numeric.indexOf(",") == numeric.length - 3
                    || numeric.contains(".") && numeric.indexOf(".") == numeric.length - 3) {
                val integer = numbers.substring(0, numbers.length - 2)
                val decimal = numbers.substring(numbers.length - 2)
                return if (decimal == "00") integer else "$integer.$decimal"
            }
            return numbers
        }
        return EMPTY
    }

    fun unmaskCPF(value: String?): String {
        return value?.replace("[^*0-9]".toRegex(), EMPTY) ?: EMPTY
    }

    fun addBRCurrency(text: String): String {
        return "R$ $text"
    }

    fun convertToDecimal(value: Double): String {
        return decimalFormat.format(value)
    }

    fun prepareValueBRCurrencyApi(text: String?): String {
        if (text == null || text == EMPTY) {
            return EMPTY
        }
        val numbers = text.replace("[^0-9]+".toRegex(), EMPTY)
        val sb = StringBuilder(numbers)
        sb.insert(numbers.length - 2, ".")
        return sb.toString()
    }

    fun removeBRCurrencyAndSpecialChars(text: String?): String {
        return text?.replace("[$,.R\\u2007\\u202F\\u00A0 ]".toRegex(), EMPTY) ?: EMPTY
    }

    fun isNullOrEmpty(value: String?): Boolean {
        return value == null || value.isEmpty()
    }

}