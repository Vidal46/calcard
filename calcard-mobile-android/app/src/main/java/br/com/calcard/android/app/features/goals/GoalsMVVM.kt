package br.com.calcard.android.app.features.goals

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import br.com.calcard.android.app.model.Goals
import br.com.calcard.android.app.model.Limits

interface GoalsMVVM {
    interface View {
        fun setAllVariables()
        fun setGoalsAdapter(goals: List<Goals>?)
        fun setAllBottomSheets()
        fun notifyAnimateTransitionBack()
        fun notifyBackEvent()
        fun notifyOpenInfo()
        fun notifyDismissInfo()
        fun notifyBottomSheet()
        fun notifyDismissBottomSheet()
        fun notifyShowError(message: String?)
        fun notifyBottomSheetSuccess()
        fun notifyBottomSheetError()
    }

    interface ViewModel {
        fun setView(view: View)
        fun onCreate()
        fun onBackPressed()
        fun onBackImagePressed()
        fun onItemClicked(item: Goals?)
        fun onInfoClicked()
        fun onInfoConfirmClicked()
        fun onBottomSheetSaveClicked()
        fun getSelectedBottomSheet(): ObservableField<Goals>
        fun getGoalValue(): ObservableField<String>
        fun isProgressEvent(): ObservableBoolean
        fun isLimitValid(): ObservableBoolean
        fun onRequestGoalsSuccess(goals: List<Goals>?)
        fun onRequestError(message: String?)
        fun onBottomSheetCancelClicked()
        fun onSaveGoalsSuccess()
        fun onSaveGoalsError()
        fun onRequestLimitsSuccess(limits: Limits?)
    }

    interface DomainModel {
        fun setViewModel(viewModel: ViewModel)
        fun requestGoals()
        fun saveGoals(goals: Goals, goalValue:String)
        fun requestLimits()
    }

}