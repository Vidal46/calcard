package br.com.calcard.android.app.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class SaveGoals constructor(@SerializedName("expenseGoalId")
                            @Expose private var expenseGoalId: Int,
                                 @SerializedName("goalValue")
                            @Expose private var goalValue: Double)
