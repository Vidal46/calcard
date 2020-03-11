package br.com.calcard.android.app.model

import java.io.Serializable

class Insurance(
        var contractable: MutableList<Contractable>?,
        var contracted: MutableList<Contracted>?
) : Serializable