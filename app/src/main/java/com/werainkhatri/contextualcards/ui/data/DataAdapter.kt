package com.werainkhatri.contextualcards.ui.data

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.werainkhatri.contextualcards.R
import com.werainkhatri.contextualcards.data.models.CardGroup
import com.werainkhatri.contextualcards.databinding.RecyclerviewCardGroupBinding
import com.werainkhatri.contextualcards.ui.card_groups.CardGroupAdapter
import com.werainkhatri.contextualcards.ui.card_groups.CardGroupFactory

class DataAdapter(
    private val cardGroups: List<CardGroup>,
    private val owner: ViewModelStoreOwner,
    private val context: Context
) : RecyclerView.Adapter<DataAdapter.DataViewHolder>() {
    private val tag = "DataAdapter"

    private lateinit var viewModel: CardGroup
    private lateinit var cardGroupFactory: CardGroupFactory

    override fun getItemCount(): Int = cardGroups.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        return DataViewHolder(
                DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.recyclerview_card_group,
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.binding.cardGroup = cardGroups[position]
        setCardsRecyclerView(holder.binding.rvCards, cardGroups[position])
    }

    private fun setCardsRecyclerView(recyclerView: RecyclerView, cardGroup: CardGroup) {
        cardGroupFactory = CardGroupFactory(cardGroup)
        viewModel = ViewModelProvider(owner, cardGroupFactory).get(CardGroup::class.java)
        recyclerView.also {
            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            it.setHasFixedSize(true)
            it.adapter = CardGroupAdapter(cardGroup.cards)
        }

    }

    inner class DataViewHolder(
            val binding: RecyclerviewCardGroupBinding
    ) : RecyclerView.ViewHolder(binding.root)
}