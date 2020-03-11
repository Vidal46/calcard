package br.com.calcard.android.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.calcard.android.app.R
import br.com.calcard.android.app.model.Contracted
import br.com.calcard.android.app.utils.CaseManipulation
import kotlinx.android.synthetic.main.my_insurance_list_item.view.*

class ContractedInsurancesAdapter(
        private val context: Context,
        private val insurances: MutableList<Contracted>,
        val onItemClickListener: (contracted: Contracted) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount() = insurances.size

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return ContractedInsurancesViewHolder(LayoutInflater.from(context).inflate(R.layout.my_insurance_list_item, parent, false), onItemClickListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ContractedInsurancesViewHolder
        insurances[position].run {
            holder.let {
                it.insuranceName.text = CaseManipulation.toCamelCase(insuranceName)
                it.bindView(this)
            }
        }
    }

    inner class ContractedInsurancesViewHolder(itemView: View, val onItemClickListener: (contracted: Contracted) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val insuranceName: TextView = itemView.my_insurance_list_item_name
        val container: View = itemView.my_insurance_list_item_container

        fun bindView(contracted: Contracted) {
            container.setOnClickListener {
                onItemClickListener.invoke(contracted)
            }
        }
    }
}