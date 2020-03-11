package br.com.calcard.android.app.features.firstunlockcard

import br.com.calcard.android.app.MyApplication
import br.com.calcard.android.app.R
import br.com.calcard.android.app.api.AppService
import br.com.calcard.android.app.model.UnlockCard
import br.com.calcard.android.app.utils.Constants
import br.com.calcard.android.app.utils.ObserverUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.HttpException

class FirstUnlockCardDomainModel(private var appService: AppService) : FirstUnlockCardMVVM.DomainModel {

    private lateinit var viewModel: FirstUnlockCardMVVM.ViewModel

    override fun setViewModel(viewModel: FirstUnlockCardMVVM.ViewModel) {
        this.viewModel = viewModel
    }

    override fun requestUnlockCard(cvv: String, pwd: String) {
        appService.doUnlockCard(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser", null),
                MyApplication.preferences.getString("card", null), object : UnlockCard(pwd, pwd, cvv) {})
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ObserverUtils<ResponseBody>() {
                    override fun onComplete() {
                        viewModel.onUnlockSuccess()
                    }

                    override fun onError(e: Throwable) {
                        var message: String
                        if ((e as HttpException).code() >= 500) {
                            message = MyApplication.getContext().getString(R.string.unlock_card_default_error)
                        } else {
                            val jObjError: JSONObject = JSONArray(e.response().errorBody()?.string()).getJSONObject(0)
                            message = jObjError.getString("message")
                        }
                        viewModel.onUnlockError(message)
                    }
                })
    }

}