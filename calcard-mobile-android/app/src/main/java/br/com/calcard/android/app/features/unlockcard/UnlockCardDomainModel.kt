package br.com.calcard.android.app.features.unlockcard

import br.com.calcard.android.app.MyApplication
import br.com.calcard.android.app.api.AppService
import br.com.calcard.android.app.model.UnlockCard
import br.com.calcard.android.app.utils.Constants
import br.com.calcard.android.app.utils.ObserverUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody

class UnlockCardDomainModel(private var appService: AppService) : UnlockCardMVVM.DomainModel {

    private lateinit var viewModel: UnlockCardMVVM.ViewModel

    override fun setViewModel(viewModel: UnlockCardMVVM.ViewModel) {
        this.viewModel = viewModel
    }

    override fun requestUnlockCard() {
        appService.doUnlockCard(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser", null),
                MyApplication.preferences.getString("card", null), object : UnlockCard("", "", "") {})
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ObserverUtils<ResponseBody>() {
                    override fun onComplete() {
                        viewModel.onUnlockSuccess()
                    }

                    override fun onError(e: Throwable) {
                        viewModel.onUnlockError()
                    }
                })
    }

}