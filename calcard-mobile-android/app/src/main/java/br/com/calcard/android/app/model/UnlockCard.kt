package br.com.calcard.android.app.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class UnlockCard constructor(@SerializedName("confirmPassword")
                                  @Expose private var confirmPassword: String,
                                  @SerializedName("password")
                                  @Expose private var password: String,
                                  @SerializedName("cvv")
                                  @Expose private var cvv: String)