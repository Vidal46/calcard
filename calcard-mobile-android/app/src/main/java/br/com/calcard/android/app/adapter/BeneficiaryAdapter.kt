package br.com.calcard.android.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.calcard.android.app.R
import br.com.calcard.android.app.model.Beneficiary
import kotlinx.android.synthetic.main.adding_beneficiary_list_item.view.*

class BeneficiaryAdapter(
        private val context: Context,
        private val beneficiaries: MutableList<Beneficiary>,
        val onItemClickListner: (beneficiary: Beneficiary) -> Unit,
        val secondOnclickListener: (beneficiary: Beneficiary) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount() = beneficiaries.size

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return BeneficiaryViewHolder(LayoutInflater.from(context).inflate(R.layout.adding_beneficiary_list_item, parent, false), onItemClickListner, secondOnclickListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as BeneficiaryViewHolder
        beneficiaries[position].run {
            holder.let {
                it.beneficiaryName.text = name
                it.beneficiaryPercentage.text = "$percentage%"
                it.bindView(this)
            }
        }
    }

    inner class BeneficiaryViewHolder(itemView: View, val onItemClickListner: (beneficiary: Beneficiary) -> Unit, val secondOnclickListener: (beneficiary: Beneficiary) -> Unit) : RecyclerView.ViewHolder(itemView) {
        var beneficiaryName: TextView = itemView.adding_beneficiary_list_item_name
        var beneficiaryPercentage: TextView = itemView.adding_beneficiary_list_item_percentage_value
        var beneficiaryContainer: View = itemView.adding_beneficiary_container
        var beneficiaryDeleteIcon: ImageView = itemView.adding_beneficiary_list_item_icon

        fun bindView(beneficiary: Beneficiary) {
            beneficiaryDeleteIcon.setOnClickListener {
                onItemClickListner.invoke(beneficiary)
            }

            beneficiaryContainer.setOnClickListener {
                secondOnclickListener.invoke(beneficiary)
            }
        }
    }
}