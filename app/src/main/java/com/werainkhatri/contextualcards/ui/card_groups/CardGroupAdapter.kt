package com.werainkhatri.contextualcards.ui.card_groups

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.werainkhatri.contextualcards.R
import com.werainkhatri.contextualcards.data.models.Card
import com.werainkhatri.contextualcards.databinding.RecyclerviewCardBinding

class CardGroupAdapter(private val cards: List<Card>) : RecyclerView.Adapter<CardGroupAdapter.CardGroupHolder>() {

    override fun getItemCount(): Int = cards.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardGroupHolder = CardGroupHolder(
            DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.recyclerview_card,
                    parent,
                    false
            )
    )

    override fun onBindViewHolder(holder: CardGroupHolder, position: Int) {
        holder.binding.card = cards[position]
    }

    inner class CardGroupHolder(
            val binding: RecyclerviewCardBinding
    ) : RecyclerView.ViewHolder(binding.root)
}