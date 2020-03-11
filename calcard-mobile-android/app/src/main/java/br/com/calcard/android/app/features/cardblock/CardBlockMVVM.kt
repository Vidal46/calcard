package br.com.calcard.android.app.features.cardblock

import androidx.databinding.ObservableBoolean

interface CardBlockMVVM {
    interface View {
        fun notifyDismissBottomSheet()
        fun notifyCardBlockError()
        fun notifyCardBlockSuccess()
    }

    interface ViewModel {
        fun setView(view: View)
        fun onCancelClicked()
        fun onConfirmClicked()
        fun onCardBlockSuccess()
        fun onCardBlockError()
        fun onCreateView()
        fun isSuccess(): ObservableBoolean
        fun isProgressEvent(): ObservableBoolean
    }

    interface DomainModel {
        fun setViewModel(viewModel: ViewModel)
        fun requestCardBlock()
    }

}