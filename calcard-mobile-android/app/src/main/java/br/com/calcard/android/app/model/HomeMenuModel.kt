package br.com.calcard.android.app.model

import br.com.calcard.android.app.ui.ouze.HomeItemEnum
import java.io.Serializable

class HomeMenuModel(
        val itens: ArrayList<HomeItemEnum> = arrayListOf(
                HomeItemEnum.MINHA_FATURA,
                HomeItemEnum.TIMELINE,
                HomeItemEnum.MEUS_DADOS,
                HomeItemEnum.METAS,
                HomeItemEnum.DESBLOQUEAR_CARTAO,
                HomeItemEnum.BLOQUEAR_CARTAO,
                HomeItemEnum.PERDA_E_ROUBO,
                HomeItemEnum.MINHAS_SENHAS,
//                HomeItemEnum.SEGUROS_ASSISTENCIAS,
                HomeItemEnum.CONTRATO,
                HomeItemEnum.SAIR
        )

) : Serializable