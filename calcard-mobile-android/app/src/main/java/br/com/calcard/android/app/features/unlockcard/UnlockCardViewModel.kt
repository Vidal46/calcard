package br.com.calcard.android.app.features.unlockcard

import androidx.databinding.ObservableBoolean

class UnlockCardViewModel(private var model: UnlockCardMVVM.DomainModel) : UnlockCardMVVM.ViewModel {

    private lateinit var view: UnlockCardMVVM.View

    private var isProgress: ObservableBoolean = ObservableBoolean(false)
    private var isSuccess: ObservableBoolean = ObservableBoolean(false)

    override fun setView(view: UnlockCardMVVM.View) {
        this.view = view
    }

    override fun onCreateView() {
        model.setViewModel(this)
    }

    override fun onCancelClicked() {
        isProgress.set(false)
        isSuccess.set(false)
        view.notifyDismissBottomSheet()
    }

    override fun onConfirmClicked() {
        isProgress.set(true)
        model.requestUnlockCard()
    }

    override fun onUnlockSuccess() {
        isProgress.set(false)
        isSuccess.set(true)
        view.notifyUnlockCardSuccess()
    }

    override fun onUnlockError() {
        isProgress.set(false)
        view.notifyUnlockCardError()
    }

    override fun isProgress(): ObservableBoolean {
        return isProgress
    }

    override fun isSuccess(): ObservableBoolean {
        return isSuccess
    }
}