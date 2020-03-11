package br.com.calcard.android.app.model

data class LastTransaction(
        var accountId: Int,
        var credit: Boolean,
        var description: String,
        var descriptionStatus: String,
        var disputeRequested: Boolean,
        var dolarValue: Double,
        var establishment: String,
        var establishmentType: String,
        var eventDate: String,
        var expenseGoal: ExpenseGoal,
        var installmentValue: Double,
        var installments: Int,
        var lastPostedInstallment: String,
        var mccGroupId: Int,
        var order: Int,
        var registryType: String,
        var status: String,
        var transactionId: Int,
        var transactionType: String,
        var value: Double
)