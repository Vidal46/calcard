package br.com.calcard.android.app.ui.invoice

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.calcard.android.app.R
import br.com.calcard.android.app.model.Transaction
import br.com.calcard.android.app.utils.FormatterUtil


typealias ItemClicado = (transaction: Transaction) -> Unit

class InvoiceMenuAdapter(
        val context: Context,
        val itens: MutableList<Transaction>,
        private val onclick: ItemClicado
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.invoice_component, parent, false)
        return InvoiceViewHolder(view)
    }

    override fun getItemCount() = itens.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as InvoiceViewHolder
        holder.run {
            val item = itens[position]

            title.text = item.establishmentName
            value.text = FormatterUtil.formatCurrency(item.real)
            time.text = FormatterUtil.formatDate(item.transactionDateTime)
            img.setImageResource(getIcon(item.expenseGoalName))

            if (item.installmentQuantity != null && item.installment != null && item.installmentQuantity > 0) {
                parcelas.visibility = View.VISIBLE
                parcelas.text = (item.installment.toString() + "/" + item.installmentQuantity)
            }
        }
    }

    inner class InvoiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.invoice_component_title)
        var value: TextView = itemView.findViewById(R.id.invoice_component_text)
        var time: TextView = itemView.findViewById(R.id.invoice_component_time)
        var img: ImageView = itemView.findViewById(R.id.invoice_component_icon)
        var parcelas: TextView = itemView.findViewById(R.id.invoice_component_parcela)

        init {
            itemView.setOnClickListener {
                onclick.invoke(itens[adapterPosition])
            }
        }
    }

    fun getIcon(tipo: String): Int {
        return when (tipo.toLowerCase()) {
            "mercado" -> R.drawable.ic_supermercado
            "restaurante" -> R.drawable.ic_restaurante
            "lazer" -> R.drawable.ic_lazer
            "saude" -> R.drawable.ic_saude
            "transporte" -> R.drawable.ic_transporte

            else -> {
                R.drawable.ic_outros
            }
        }

    }
}
