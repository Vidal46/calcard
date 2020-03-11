package br.com.calcard.android.app.features.goals

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import br.com.calcard.android.app.model.Goals
import br.com.calcard.android.app.model.Limits
import br.com.calcard.android.app.utils.StringUtil

class GoalsViewModel(private var model: GoalsMVVM.DomainModel) : GoalsMVVM.ViewModel {

    private lateinit var view: GoalsMVVM.View

    private var selectedGoals: ObservableField<Goals> = ObservableField()
    private var goalValue: ObservableField<String> = ObservableField()
    private var progress: ObservableBoolean = ObservableBoolean()
    private var isLimitValid: ObservableBoolean = ObservableBoolean()
    private var limit: ObservableField<String> = ObservableField()

    override fun setView(view: GoalsMVVM.View) {
        this.view = view
    }

    override fun onCreate() {
        model.setViewModel(this)
        progress.set(true)
        isLimitValid.set(true)
        model.requestLimits()
        model.requestGoals()
        view.setAllVariables()
        view.setAllBottomSheets()
    }

    override fun onBackPressed() {
        view.notifyAnimateTransitionBack()
    }

    override fun onBackImagePressed() {
        view.notifyBackEvent()
    }

    override fun onItemClicked(item: Goals?) {
        selectedGoals.set(item)
        goalValue.set(item?.goal.toString())
        view.notifyBottomSheet()
    }

    override fun onInfoClicked() {
        view.notifyOpenInfo()
    }

    override fun onInfoConfirmClicked() {
        view.notifyDismissInfo()
    }

    override fun onBottomSheetSaveClicked() {
        if (StringUtil.prepareValueBRCurrencyApi(goalValue.get()).toDouble() <= limit.get()?.toDouble()!!) {
            isLimitValid.set(true)
            view.notifyDismissBottomSheet()
            progress.set(true)
            selectedGoals.get()?.let { goalValue.get()?.let { it1 -> model.saveGoals(it, it1) } }
        } else {
            isLimitValid.set(false)
        }
    }

    override fun getSelectedBottomSheet(): ObservableField<Goals> {
        return selectedGoals
    }

    override fun getGoalValue(): ObservableField<String> {
        return goalValue
    }

    override fun isProgressEvent(): ObservableBoolean {
        return progress
    }

    override fun isLimitValid(): ObservableBoolean {
        return isLimitValid
    }

    override fun onRequestGoalsSuccess(goals: List<Goals>?) {
        progress.set(false)
        view.setGoalsAdapter(goals)
    }

    override fun onRequestError(message: String?) {
        progress.set(false)
        view.notifyShowError(message)
    }

    override fun onBottomSheetCancelClicked() {
        view.notifyDismissBottomSheet()
    }

    override fun onSaveGoalsSuccess() {
        view.notifyBottomSheetSuccess()
        progress.set(true)
        model.requestGoals()
    }

    override fun onSaveGoalsError() {
        view.notifyBottomSheetError()
    }

    override fun onRequestLimitsSuccess(limits: Limits?) {
        limit.set(limits?.withdraw.toString())
    }

}