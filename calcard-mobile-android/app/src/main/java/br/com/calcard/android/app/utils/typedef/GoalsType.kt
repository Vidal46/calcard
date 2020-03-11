package br.com.calcard.android.app.utils.typedef

import android.graphics.drawable.Drawable
import androidx.annotation.StringDef
import br.com.calcard.android.app.MyApplication
import br.com.calcard.android.app.R
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

object GoalsType {
    const val TRANSPORT = "Transporte"
    const val OTHERS = "Outros"
    const val FOOD = "Restaurante"
    const val HEALTH = "Saúde"
    const val RECREATION = "Lazer"
    const val MARKET = "Mercado"

    @StringDef(TRANSPORT, MARKET, HEALTH, FOOD, OTHERS, RECREATION)
    @Retention(RetentionPolicy.SOURCE)
    annotation class GoalsIDDef

    fun getIconsByType(name: String): Drawable? {
        return when (name) {
            TRANSPORT -> MyApplication.getContext().getDrawable(R.drawable.ic_transporte)
            MARKET -> MyApplication.getContext().getDrawable(R.drawable.ic_supermercado)
            HEALTH -> MyApplication.getContext().getDrawable(R.drawable.ic_saude)
            FOOD -> MyApplication.getContext().getDrawable(R.drawable.ic_restaurante)
            OTHERS -> MyApplication.getContext().getDrawable(R.drawable.ic_outros)
            RECREATION -> MyApplication.getContext().getDrawable(R.drawable.ic_esportes)
            else -> null
        }
    }

    fun getGoalPercentage(spent: Double?, goal: Double?): Int? {
        return if (spent == 0.0 || goal == 0.0)
            0
        else
            (spent?.toInt()?.times(100))?.div(goal?.toInt()!!)
    }
}
