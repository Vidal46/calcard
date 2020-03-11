package br.com.calcard.android.app.ui.ouze

import java.io.Serializable

enum class CpfSituation : Serializable {
    MIGRATION,
    LOGIN,
    REGISTRATION,
    PROSPECT
}