package org.stepic.droid.ui.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.view_code_toolbar_item.view.*
import org.stepic.droid.R
import org.stepic.droid.model.code.symbolsForLanguage
import org.stepic.droid.ui.listeners.OnItemClickListener

class CodeToolbarAdapter(
        language: String,
        context: Context,
        private val onSymbolClickListener: OnSymbolClickListener
) : RecyclerView.Adapter<CodeToolbarAdapter.Companion.CodeToolbarItem>() {

    interface OnSymbolClickListener {
        fun onSymbolClick(symbol: String)
    }

    private val symbols = symbolsForLanguage(language, context)
    private val onItemClickListener = OnItemClickListener { position ->
        val symbol = symbols[position]
        onSymbolClickListener.onSymbolClick(symbol)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CodeToolbarItem? {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_code_toolbar_item, parent, false)
        return CodeToolbarItem(view, onItemClickListener)
    }

    override fun onBindViewHolder(holder: CodeToolbarItem, position: Int) {
        holder.bindData(symbols[position])
    }

    override fun getItemCount(): Int = symbols.size

    companion object {
        class CodeToolbarItem(itemView: View, onItemClickListener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {

            init {
                itemView.setOnClickListener {
                    onItemClickListener.onItemClick(adapterPosition)
                }
            }

            fun bindData(symbol: String) {
                itemView.codeToolbarSymbol.text = symbol
            }
        }
    }

}
