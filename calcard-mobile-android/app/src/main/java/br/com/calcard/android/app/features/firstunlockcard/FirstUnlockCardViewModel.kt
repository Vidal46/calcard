package br.com.calcard.android.app.features.firstunlockcard

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField

class FirstUnlockCardViewModel(private var model: FirstUnlockCardMVVM.DomainModel) : FirstUnlockCardMVVM.ViewModel {

    private lateinit var view: FirstUnlockCardMVVM.View

    private var cvvValue: ObservableField<String> = ObservableField("")
    private var pwdValue: ObservableField<String> = ObservableField("")
    private var isProgress: ObservableBoolean = ObservableBoolean(false)
    private var isCvvValid: ObservableBoolean = ObservableBoolean(false)
    private var isPwdValid: ObservableBoolean = ObservableBoolean(false)
    private var isCvvInfoVisible: ObservableBoolean = ObservableBoolean(false)
    private var isCvvInputVisible: ObservableBoolean = ObservableBoolean(true)
    private var isPwdInputVisible: ObservableBoolean = ObservableBoolean(false)
    private var isSuccess: ObservableBoolean = ObservableBoolean(false)
    private var isError: ObservableBoolean = ObservableBoolean(false)
    private var errorMessage: ObservableField<String> = ObservableField("")

    override fun setView(view: FirstUnlockCardMVVM.View) {
        this.view = view
    }

    override fun onCreateView() {
        model.setViewModel(this)
        view.setCvvWatcher()
        view.setPwdWatcher()
    }

    override fun afterCvvTextChanged() {
        if (cvvValue.get()?.length == 3) {
            isCvvValid.set(true)
        } else {
            isCvvValid.set(false)
        }
    }

    override fun afterPwdTextChanged() {
        if (pwdValue.get()?.length == 4) {
            isPwdValid.set(true)
        } else {
            isPwdValid.set(false)
        }
    }

    override fun onCancelClicked() {
        isCvvInfoVisible.set(false)
        isCvvInputVisible.set(true)
        isPwdInputVisible.set(false)
        isSuccess.set(false)
        isError.set(false)
        cvvValue.set("")
        pwdValue.set("")
        view.notifyDismissBottomSheet()
    }

    override fun onCvvInfoClicked() {
        isCvvInfoVisible.set(true)
        isCvvInputVisible.set(false)
    }

    override fun onCvvInfoButtomClicked() {
        isCvvInfoVisible.set(false)
        isCvvInputVisible.set(true)
    }

    override fun onCvvNextButtomClicked() {
        isCvvInputVisible.set(false)
        isPwdInputVisible.set(true)
        view.notifyCvvImeActionDone()
    }

    override fun onPwdNextButtomClicked() {
        isPwdInputVisible.set(false)
        isProgress.set(true)
        cvvValue.get()?.let { pwdValue.get()?.let { it1 -> model.requestUnlockCard(it, it1) } }
        view.notifyPwdImeActionDone()
    }

    override fun getCvvValue(): ObservableField<String> {
        return cvvValue
    }

    override fun getPwdValue(): ObservableField<String> {
        return pwdValue
    }

    override fun getErrorMessage(): ObservableField<String> {
        return errorMessage
    }

    override fun isProgress(): ObservableBoolean {
        return isProgress
    }

    override fun isCvvValid(): ObservableBoolean {
        return isCvvValid
    }

    override fun isPwdValid(): ObservableBoolean {
        return isPwdValid
    }

    override fun isCvvInfoVisible(): ObservableBoolean {
        return isCvvInfoVisible
    }

    override fun isCvvInputVisible(): ObservableBoolean {
        return isCvvInputVisible
    }

    override fun isPwdInputVisible(): ObservableBoolean {
        return isPwdInputVisible
    }

    override fun isSuccess(): ObservableBoolean {
        return isSuccess
    }

    override fun isError(): ObservableBoolean {
        return isError
    }

    override fun onUnlockSuccess() {
        isProgress.set(false)
        isSuccess.set(true)
        view.notifyUnlockCardSuccess()
    }

    override fun onUnlockError(message: String) {
        errorMessage.set(message)
        isProgress.set(false)
        isError.set(true)
    }

}