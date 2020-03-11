package br.com.calcard.android.app.model

import java.io.Serializable

class Vehicle(
        var color: String?,
        var detail: String?,
        var make: String?,
        var manufacturingYear: Long?,
        var model: String?,
        var modelYear: Long?,
        var plate: String?,
        var vehicleIdentificationNumber: String?
) : Serializable