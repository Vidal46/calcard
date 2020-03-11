package br.com.calcard.android.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.calcard.android.app.R
import br.com.calcard.android.app.model.Coverage
import kotlinx.android.synthetic.main.hiring_insurance_list_item.view.*

class HiringInsuranceDetailsAdapter(
        private val context: Context,
        private val coverages: MutableList<Coverage>,
        private val cancel: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount() = coverages.size

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return HiringInsuranceDetailsViewHolder(LayoutInflater.from(context).inflate(R.layout.hiring_insurance_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as HiringInsuranceDetailsViewHolder
        coverages[position].run {
            holder.let {
                it.coverageName.text = description
                it.coverageInfo.text = info
            }
        }
    }

    inner class HiringInsuranceDetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var coverageName: TextView = itemView.hiring_insurance_list_item_name
        var coverageInfo: TextView = itemView.hiring_insurance_list_item_text
    }
}