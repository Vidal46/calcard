package br.com.calcard.android.app.model

import java.io.Serializable

class Contracted(
        var certificateNumber: String?,
        var effectiveDate: String?,
        var id: Long?,
        var insuranceName: String?,
        var luckyNumber: String
) : Serializable