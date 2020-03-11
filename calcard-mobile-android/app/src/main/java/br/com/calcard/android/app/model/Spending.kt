package br.com.calcard.android.app.model

class Spending(
        val accountId: Int?,
        val registryType: String?,
        val order: Int?,
        val transactionId: Int?,
        val description: String?,
        val status: String?,
        val descriptionStatus: String?,
        val value: Double?,
        val dolarValue: Double?,
        val installments: Int?,
        val eventDate: String?,
        val establishment: String?,
        val credit: Boolean?,
        val establishmentType: String?,
        val mccGroupId: Int?,
        val disputeRequested: Boolean?,
        val transactionType: String?,
        val lastPostedInstallment: String?,
        val expenseGoal: ExpenseGoal?
)