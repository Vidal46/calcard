package br.com.calcard.android.app.features.goals.viewcomponents

import android.app.Activity
import br.com.calcard.android.app.R
import br.com.calcard.android.app.common.adapter.BindingViewHolder
import br.com.calcard.android.app.common.adapter.DataBindingAdapter
import br.com.calcard.android.app.databinding.ItemGoalsListBinding
import br.com.calcard.android.app.features.goals.GoalsMVVM
import br.com.calcard.android.app.model.Goals
import br.com.calcard.android.app.utils.CurrencyHelper
import br.com.calcard.android.app.utils.typedef.GoalsType

open class GoalsListAdapter(activity: Activity?, private val viewModel: GoalsMVVM.ViewModel) : DataBindingAdapter<ItemGoalsListBinding?, Goals?>(activity!!) {

    override val itemResource: Int
        get() = R.layout.item_goals_list

    override fun onBindViewHolder(holder: BindingViewHolder<ItemGoalsListBinding?>, position: Int) {
        holder.binding?.icon?.background = getItems()[position]?.name?.let { GoalsType.getIconsByType(it) }
        holder.binding?.iconTitle?.text = getItems()[position]?.name
        holder.binding?.txvSpent?.text = getItems()[position]?.spent?.let { CurrencyHelper.format(it) }
        holder.binding?.txvGoal?.text = getItems()[position]?.goal?.let { CurrencyHelper.format(it) }
        holder.binding?.iconProgress?.progress = GoalsType.getGoalPercentage(getItems()[position]?.spent, getItems()[position]?.goal)!!
        holder.binding?.goalContainer?.setOnClickListener { viewModel.onItemClicked(getItems()[position]) }

    }
}