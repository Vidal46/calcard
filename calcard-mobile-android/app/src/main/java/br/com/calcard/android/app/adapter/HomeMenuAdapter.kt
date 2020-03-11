package br.com.calcard.android.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.calcard.android.app.R
import br.com.calcard.android.app.ui.ouze.HomeItemEnum

typealias ItemClicado = (lista: HomeItemEnum) -> Unit

class HomeMenuAdapter(
        val context: Context,
        val itens: ArrayList<HomeItemEnum>,
        private val onclick: ItemClicado
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.home_card_view, parent, false)
        return MenuViewHolder(view)
    }

    override fun getItemCount() = itens.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as MenuViewHolder
        holder.run {
            val item = itens[position]

            when (item) {
                HomeItemEnum.MINHA_FATURA -> {
                    title.text = context.getText(R.string.home_menu_invoice)
                    icon.setImageResource(R.drawable.ic_minha_fatura)
                }
                HomeItemEnum.MEUS_DADOS -> {
                    title.text = context.getText(R.string.home_menu_profile)
                    icon.setImageResource(R.drawable.ic_meus_dados)
                }
                HomeItemEnum.METAS -> {
                    title.text = context.getText(R.string.home_menu_goals)
                    icon.setImageResource(R.drawable.ic_metas)
                }
                HomeItemEnum.BLOQUEAR_CARTAO -> {
                    title.text = context.getText(R.string.home_menu_temporary_block)
                    icon.setImageResource(R.drawable.ic_bloquear_cartao)
                }
                HomeItemEnum.DESBLOQUEAR_CARTAO -> {
                    title.text = context.getText(R.string.home_menu_unblock_card)
                    icon.setImageResource(R.drawable.ic_desbloquear_cartao)
                }
                HomeItemEnum.MINHAS_SENHAS -> {
                    title.text = context.getText(R.string.home_menu_passwords)
                    icon.setImageResource(R.drawable.ic_minhas_senhas)
                }
//                HomeItemEnum.SEGUROS_ASSISTENCIAS -> {
//                    title.text = context.getText(R.string.home_menu_assist)
//                    icon.setImageResource(R.drawable.ic_seguros)
//                }
                HomeItemEnum.CONTRATO -> {
                    title.text = context.getText(R.string.home_menu_contract)
                    icon.setImageResource(R.drawable.ic_contrato)
                }
                HomeItemEnum.TIMELINE -> {
                    title.text = "Linha do\ntempo"
                    icon.setImageResource(R.drawable.ic_timeline)
                }
                HomeItemEnum.PERDA_E_ROUBO -> {
                    title.text = "Perda\ne Roubo"
                    icon.setImageResource(R.drawable.ic_menu_perda_roubo)
                }
                HomeItemEnum.SAIR -> {
                    title.text = context.getText(R.string.home_menu_exit)
                    icon.setImageResource(R.drawable.menu_quit)
                }
            }

        }

    }

    inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.home_card_view_title)
        var icon: ImageView = itemView.findViewById(R.id.home_card_icon)

        init {
            itemView.setOnClickListener {
                onclick.invoke(itens[adapterPosition])
            }
        }
    }
}
