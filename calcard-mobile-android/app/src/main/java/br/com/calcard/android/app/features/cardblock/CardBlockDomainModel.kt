package br.com.calcard.android.app.features.cardblock

import br.com.calcard.android.app.MyApplication
import br.com.calcard.android.app.api.AppService
import br.com.calcard.android.app.utils.Constants
import br.com.calcard.android.app.utils.ObserverUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody

class CardBlockDomainModel(private var appService: AppService) : CardBlockMVVM.DomainModel {

    private lateinit var viewModel: CardBlockMVVM.ViewModel

    override fun setViewModel(viewModel: CardBlockMVVM.ViewModel) {
        this.viewModel = viewModel
    }

    override fun requestCardBlock() {
        appService.doCardBlock(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser", null),
                MyApplication.preferences.getString("card", null))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ObserverUtils<ResponseBody>() {
                    override fun onComplete() {
                        viewModel.onCardBlockSuccess()
                    }

                    override fun onError(e: Throwable) {
                        viewModel.onCardBlockError()
                    }
                })
    }

}

