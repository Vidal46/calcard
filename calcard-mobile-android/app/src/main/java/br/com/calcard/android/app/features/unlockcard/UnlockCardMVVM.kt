package br.com.calcard.android.app.features.unlockcard

import androidx.databinding.ObservableBoolean

interface UnlockCardMVVM {
    interface View {
        fun notifyDismissBottomSheet()
        fun notifyUnlockCardSuccess()
        fun notifyUnlockCardError()
    }

    interface ViewModel {
        fun setView(view: View)
        fun onCreateView()
        fun onConfirmClicked()
        fun onCancelClicked()
        fun onUnlockSuccess()
        fun onUnlockError()
        fun isProgress(): ObservableBoolean
        fun isSuccess(): ObservableBoolean
    }

    interface DomainModel {
        fun setViewModel(viewModel: ViewModel)
        fun requestUnlockCard()
    }
}