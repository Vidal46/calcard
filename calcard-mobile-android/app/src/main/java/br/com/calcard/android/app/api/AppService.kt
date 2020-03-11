package br.com.calcard.android.app.api

import android.content.Context
import br.com.calcard.android.app.model.Goals
import br.com.calcard.android.app.model.Limits
import br.com.calcard.android.app.model.SaveGoals
import br.com.calcard.android.app.model.UnlockCard
import io.reactivex.Observable
import okhttp3.ResponseBody
import javax.inject.Singleton

@Singleton
class AppService(private val api: Api, private val context: Context) {

    fun doRequestGoals(token: String?,
                       tokenUser: String?,
                       id: String?): Observable<List<Goals>> {
        return api.doRequestGoals(token, tokenUser, id)
    }

    fun doSaveGoals(token: String?,
                    tokenUser: String?,
                    id: String?,
                    goal: SaveGoals?): Observable<ResponseBody> {
        return api.doSaveGoal(token, tokenUser, id, goal)
    }

    fun doGetLimits(token: String?,
                    tokenUser: String?,
                    id: String?): Observable<Limits> {
        return api.doGetLimits(token, tokenUser, id)
    }

    fun doCardBlock(token: String?,
                    tokenUser: String?,
                    id: String?): Observable<ResponseBody> {
        return api.doCardBlock(token, tokenUser, id)
    }

    fun doUnlockCard(token: String?,
                     tokenUser: String?,
                     id: String?,
                     unlockCard: UnlockCard?): Observable<ResponseBody> {
        return api.doUnlockCard(token, tokenUser, id, unlockCard)
    }

}