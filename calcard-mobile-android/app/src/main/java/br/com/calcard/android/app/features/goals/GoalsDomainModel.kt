package br.com.calcard.android.app.features.goals

import br.com.calcard.android.app.MyApplication
import br.com.calcard.android.app.api.AppService
import br.com.calcard.android.app.model.Goals
import br.com.calcard.android.app.model.Limits
import br.com.calcard.android.app.model.SaveGoals
import br.com.calcard.android.app.utils.Constants
import br.com.calcard.android.app.utils.ObserverUtils
import br.com.calcard.android.app.utils.StringUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody

class GoalsDomainModel(private var appService: AppService) : GoalsMVVM.DomainModel {

    private lateinit var viewModel: GoalsMVVM.ViewModel

    override fun setViewModel(viewModel: GoalsMVVM.ViewModel) {
        this.viewModel = viewModel
    }

    override fun requestGoals() {
        appService.doRequestGoals(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser",
                null), MyApplication.preferences.getString("idAccount", null))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ObserverUtils<List<Goals>>() {
                    override fun onComplete() {
                        viewModel.onRequestGoalsSuccess(genericVariable)
                    }

                    override fun onError(e: Throwable) {
                        viewModel.onRequestError(e.message)
                    }
                })

    }

    override fun saveGoals(goals: Goals, goalValue: String) {
        appService.doSaveGoals(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser",
                null), MyApplication.preferences.getString("idAccount", null),
                object : SaveGoals(goals.expenseGoalId, StringUtil.prepareValueBRCurrencyApi(goalValue).toDouble()) {})
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ObserverUtils<ResponseBody>() {
                    override fun onComplete() {
                        viewModel.onSaveGoalsSuccess()
                    }

                    override fun onError(e: Throwable) {
                        viewModel.onSaveGoalsError()
                    }
                })
    }

    override fun requestLimits() {
        appService.doGetLimits(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser", null),
                MyApplication.preferences.getString("idAccount", null))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ObserverUtils<Limits>() {
                    override fun onComplete() {
                        viewModel.onRequestLimitsSuccess(genericVariable)
                    }

                    override fun onError(e: Throwable) {
                        viewModel.onRequestError(e.message)
                    }
                })
    }

}