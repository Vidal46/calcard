package br.com.calcard.android.app.model

import java.io.Serializable

class RequestSaleInsuranceDTO(
        var accountId: Long?,
        var beneficiaries: List<Beneficiary>,
        var cardId: Long?,
        var insuranceId: Long?,
        var password: String?,
        var vehicle: Vehicle?
) : Serializable