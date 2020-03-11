package br.com.calcard.android.app.model

data class FullInsurance (
        var beneficiaries: MutableList<Beneficiary>?,
        var certificateNumber: String?,
        var effectiveDate: String?,
        var id: Long?,
        var insurance: InsuranceDetail?,
        var insuranceName: String?,
        var luckyNumber: String?,
        var vehicle: Vehicle?
)