package br.com.calcard.android.app.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import org.joda.time.DateTime
import java.text.DateFormatSymbols
import java.util.*


class FormatterUtil {

    companion object {
        fun formatCurrency(value: Double?): String = String.format(Locale("pt", "BR"), "R$ %,.2f", value)

        fun getUpdatedAt(): String = "Atualizado em: ${DateTime.now().toString("dd MMM yyyy - hh:mm:ss").toUpperCase(getDefaultLocale())}"

        fun getBrandDrawable(context: Context, carrier: String): Drawable? {
            carrier.replace("cartao ", "").trim { it <= ' ' }.replace(" ", "_").toLowerCase().run {
                val resourceId = context.resources.getIdentifier("ic_$this", "drawable", context.packageName)
                if (resourceId != 0) {
                    return ContextCompat.getDrawable(context, resourceId)
                }
            }

            return null
        }


        fun LongestWord(sen: String): String {

            var quebra:List<String> = sen.split(" ");
            var maior: Int = 0
            var maiorPalavra = String()

            quebra.forEach {
                if (it.length > maior) {
                    maiorPalavra = it
                    maior = it.length
                }
            }

            return maiorPalavra;

        }

        fun getDefaultLocale(): Locale {
            return Locale("pt", "BR")
        }

        fun formatFullDate(s: String): String {
            val splittedDate = s.split("T")[0]
            val day = splittedDate.split("-")[2]
            val month =
                    splittedDate.split("-")[1]
            val year = splittedDate.split("-")[0]
            return "$day/$month/$year"
        }

        fun formatFullDateT(s: String): String {
            val splittedDate = s.split("T")[0]
            val day = splittedDate.split("-")[2]
            val month =
                    splittedDate.split("-")[1]
            val year = splittedDate.split("-")[0]
            return "$year-$month-$day"
        }

        fun formatDate(s: String): String {
            val splittedDate = s.split("T")[0]
            val day = splittedDate.split("-")[2]
            val month =
                    splittedDate.split("-")[1]
            return "$day/$month"
        }

        fun formatMonth(s: String): String? {
            val splittedDate = s.split("T")[0]
            val month =
                    DateFormatSymbols(Locale.getDefault()).months[splittedDate.split("-")[1].toInt() - 1]
            return month.subSequence(0, 3).toString()
        }


    }


}
