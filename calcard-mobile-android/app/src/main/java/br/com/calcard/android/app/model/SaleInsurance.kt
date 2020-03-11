package br.com.calcard.android.app.model

import java.io.Serializable

class SaleInsurance(
        var certificateNumber: String?,
        var effectiveDate: String?,
        var id: Long?,
        var insurance: InsuranceDetail?,
        var insuranceName: String?,
        var luckyNumber: String?
) : Serializable