package br.com.calcard.android.app.features.cardblock

import androidx.databinding.ObservableBoolean

class CardBlockViewModel(private var model: CardBlockMVVM.DomainModel) : CardBlockMVVM.ViewModel {

    private lateinit var view: CardBlockMVVM.View

    private var isSuccess: ObservableBoolean = ObservableBoolean(false)
    private var isProgressEvent: ObservableBoolean = ObservableBoolean(false)

    override fun setView(view: CardBlockMVVM.View) {
        this.view = view
    }

    override fun onCancelClicked() {
        view.notifyDismissBottomSheet()
    }

    override fun onConfirmClicked() {
        isProgressEvent.set(true)
        model.requestCardBlock()
    }

    override fun onCardBlockSuccess() {
        isProgressEvent.set(false)
        isSuccess.set(true)
        view.notifyCardBlockSuccess()
    }

    override fun onCardBlockError() {
        isProgressEvent.set(false)
        view.notifyCardBlockError()
    }

    override fun onCreateView() {
        isSuccess.set(false)
        model.setViewModel(this)
    }

    override fun isSuccess(): ObservableBoolean {
        return isSuccess
    }

    override fun isProgressEvent(): ObservableBoolean {
        return isProgressEvent
    }

}