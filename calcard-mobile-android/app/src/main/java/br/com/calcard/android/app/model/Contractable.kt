package br.com.calcard.android.app.model

import java.io.Serializable

class Contractable(
        var id: Long?,
        var name: String?,
        var price: Double?,
        var img: String?
) : Serializable