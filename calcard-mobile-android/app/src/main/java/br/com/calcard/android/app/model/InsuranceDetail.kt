package br.com.calcard.android.app.model

class InsuranceDetail(
        var id: Long?,
        var beneficiary: Boolean?,
        var coverages: MutableList<Coverage>?,
        var name: String?,
        var price: Double?,
        var vehicleData: Boolean?
)