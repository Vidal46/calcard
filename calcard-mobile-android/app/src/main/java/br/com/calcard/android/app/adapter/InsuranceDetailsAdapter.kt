package br.com.calcard.android.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.calcard.android.app.R
import br.com.calcard.android.app.model.Coverage
import br.com.calcard.android.app.utils.CaseManipulation
import br.com.calcard.android.app.utils.FormatterUtil
import kotlinx.android.synthetic.main.insurance_detail_list_item.view.*

class InsuranceDetailsAdapter(
        private val context: Context,
        private val coverages: MutableList<Coverage>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount() = coverages.size

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return InsuranceDetailsViewHolder(LayoutInflater.from(context).inflate(R.layout.insurance_detail_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as InsuranceDetailsViewHolder
        coverages[position].run {
            holder.let {
                it.coverageName.text = "${CaseManipulation.toSentenceCase(description)}"
                it.coverageInfo.text = "${CaseManipulation.toSentenceCase(info)} - ${FormatterUtil.formatCurrency(value?.toDouble())}"
            }
        }
    }


    inner class InsuranceDetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var coverageName: TextView = itemView.insurance_detail_list_item_title
        var coverageInfo: TextView = itemView.insurance_detail_list_item_text
    }
}