package br.com.calcard.android.app.features.firstunlockcard

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField

interface FirstUnlockCardMVVM {
    interface View {
        fun notifyDismissBottomSheet()
        fun notifyUnlockCardSuccess()
        fun setCvvWatcher()
        fun setPwdWatcher()
        fun notifyCvvImeActionDone()
        fun notifyPwdImeActionDone()
    }

    interface ViewModel {
        fun setView(view: View)
        fun onCreateView()
        fun afterCvvTextChanged()
        fun afterPwdTextChanged()
        fun onCancelClicked()
        fun onCvvInfoClicked()
        fun onCvvInfoButtomClicked()
        fun onCvvNextButtomClicked()
        fun onPwdNextButtomClicked()
        fun getCvvValue(): ObservableField<String>
        fun getPwdValue(): ObservableField<String>
        fun getErrorMessage(): ObservableField<String>
        fun isProgress(): ObservableBoolean
        fun isCvvValid(): ObservableBoolean
        fun isPwdValid(): ObservableBoolean
        fun isCvvInfoVisible(): ObservableBoolean
        fun isCvvInputVisible(): ObservableBoolean
        fun isPwdInputVisible(): ObservableBoolean
        fun isSuccess(): ObservableBoolean
        fun isError(): ObservableBoolean
        fun onUnlockSuccess()
        fun onUnlockError(message: String)
    }

    interface DomainModel {
        fun setViewModel(viewModel: ViewModel)
        fun requestUnlockCard(cvv: String, pwd: String)
    }
}
