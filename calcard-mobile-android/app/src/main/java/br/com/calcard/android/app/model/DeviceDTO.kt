package br.com.calcard.android.app.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class DeviceDTO constructor(@SerializedName("appVersion")
                                 @Expose private var appVersion: String,
                                 @SerializedName("deviceId")
                                 @Expose private var deviceId: String,
                                 @SerializedName("manufacturer")
                                 @Expose private var manufacturer: String,
                                 @SerializedName("model")
                                 @Expose private var model: String,
                                 @SerializedName("os")
                                 @Expose private var os: String,
                                 @SerializedName("osVersion")
                                 @Expose private var osVersion: String)
