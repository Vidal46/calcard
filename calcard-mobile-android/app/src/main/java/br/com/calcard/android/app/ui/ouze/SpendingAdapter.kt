package br.com.calcard.android.app.ui.ouze

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.calcard.android.app.R
import br.com.calcard.android.app.model.Spending
import br.com.calcard.android.app.utils.FormatterUtil

typealias ItemClicado = (spending: Spending) -> Unit

class SpendingAdapter(
        val context: Context?,
        val spendings: List<Spending>,
        val onclick: ItemClicado
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.spending_viewholder, parent, false)
        return SpendingViewHolder(view)
    }

    override fun getItemCount() = spendings.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as SpendingViewHolder
        holder.run {
            val item = spendings[position]

            title.text = item.description
            value.text = if (item.value != null) FormatterUtil.formatCurrency(item.value) else ""
            if (item.registryType.equals("LIMITE_APROVADO")) value.visibility = View.GONE
            time.text = FormatterUtil.formatDate(item.eventDate!!)
            img.setImageResource(getIcon(item.expenseGoal?.name))
        }
    }

    fun getIcon(tipo: String?): Int {
        return when (tipo?.toLowerCase()) {
            "mercado" -> R.drawable.ic_supermercado

            "restaurante" -> R.drawable.ic_restaurante

            "lazer" -> R.drawable.ic_lazer

            "saude" -> R.drawable.ic_saude

            "transporte" -> R.drawable.ic_transporte

            "outros" -> R.drawable.ic_outros

            else -> {
                R.drawable.ic_genericos
            }
        }

    }

    inner class SpendingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.spending_component_title)
        var value: TextView = itemView.findViewById(R.id.spending_component_text)
        var time: TextView = itemView.findViewById(R.id.spending_component_time)
        var img: ImageView = itemView.findViewById(R.id.spending_component_icon)

        init {
            itemView.setOnClickListener {
                onclick.invoke(spendings[adapterPosition])
            }
        }

    }
}