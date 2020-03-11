package br.com.calcard.android.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.calcard.android.app.R
import br.com.calcard.android.app.model.Contractable
import br.com.calcard.android.app.utils.CaseManipulation
import kotlinx.android.synthetic.main.available_insurance_list_item.view.*

class AvailableInsurancesAdapter(
        private val context: Context,
        private val insurances: MutableList<Contractable>,
        val onItemClickListener: (contractable: Contractable) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount() = insurances.size

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return AvailableInsurancesViewHolder(LayoutInflater.from(context).inflate(R.layout.available_insurance_list_item, parent, false), onItemClickListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as AvailableInsurancesViewHolder

        insurances[position].run {
            holder.let {
                it.insuranceName.text = CaseManipulation.toCamelCase(name)
                it.bindView(this)

                when (this.img) {
                    "moto_premiavel" -> it.container.setBackgroundResource(R.drawable.motor_viewholder)

                    "lar_premiavel" -> it.container.setBackgroundResource(R.drawable.lar_premiavel)

                    "pessoal" -> it.container.setBackgroundResource(R.drawable.pessoal)

                    "super_protecao" -> it.container.setBackgroundResource(R.drawable.super_protecao)

                    "protecao_financeira" -> it.container.setBackgroundResource(R.drawable.protecao_financeira)

                    "default" -> it.container.setBackgroundResource(R.drawable.full_unique_input_background)
                }
            }
        }
    }

    inner class AvailableInsurancesViewHolder(itemView: View, val onItemClickListener: (contractable: Contractable) -> Unit) : RecyclerView.ViewHolder(itemView) {
        var insuranceName: TextView = itemView.available_insurance_list_text
        var container: View = itemView.available_insurance_list_container

        fun bindView(contractable: Contractable) {
            container.setOnClickListener {
                onItemClickListener.invoke(contractable)
            }
        }
    }
}