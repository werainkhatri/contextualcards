package com.werainkhatri.contextualcards.ui.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.werainkhatri.contextualcards.R
import com.werainkhatri.contextualcards.data.models.CardGroup
import com.werainkhatri.contextualcards.databinding.CardviewHc3Binding
import com.werainkhatri.contextualcards.databinding.CardviewHc6Binding
import com.werainkhatri.contextualcards.databinding.RecyclerviewCardGroupBinding
import com.werainkhatri.contextualcards.ui.card_groups.CardGroupAdapter
import com.werainkhatri.contextualcards.ui.card_groups.CardGroupFactory
import com.werainkhatri.contextualcards.utils.BindUtils

class DataAdapter(
        private val cardGroups: MutableList<CardGroup>,
        private val owner: ViewModelStoreOwner,
        private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val tag = "DataAdapter"

    private lateinit var viewModel: CardGroup
    private lateinit var cardGroupFactory: CardGroupFactory
    private lateinit var rootView: View

    override fun getItemCount(): Int = cardGroups.size

    override fun getItemViewType(position: Int): Int {
        return when (cardGroups[position].design_type) {
            "HC1" -> 1
            "HC3" -> 3
            "HC5" -> 5
            "HC6" -> 6
            "HC9" -> 9
            else -> throw IllegalStateException("No card matches with design type: ${cardGroups[position].design_type}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        rootView = parent.rootView
        return when(viewType) {
            3 -> HC3CardGroupHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.cardview_hc3,
                parent,
                false
            ))
            6 -> HC6CardGroupHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.cardview_hc6,
                parent,
                false
            ))
            1, 5, 9 -> DataViewHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.recyclerview_card_group,
                parent,
                false
            ))
            else -> throw IllegalStateException("No card maps with view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DataViewHolder -> setCardsRecyclerView(holder.binding.recyclerView, cardGroups[position])
            is HC3CardGroupHolder -> BindUtils.bindHC3(holder, cardGroups[position], rootView, context) { remindLater ->
                if(!remindLater) // TODO add id of cardgroup to sharedpreferences
                    cardGroups.removeAt(position)
                notifyDataSetChanged()
            }
            is HC6CardGroupHolder -> BindUtils.bindHC6(holder, cardGroups[position], rootView, context)
            else -> throw IllegalStateException("No card matches with holder type: $holder")
        }
    }

    private fun setCardsRecyclerView(recyclerView: RecyclerView, cardGroup: CardGroup) {
        cardGroupFactory = CardGroupFactory(cardGroup)
        viewModel = ViewModelProvider(owner, cardGroupFactory).get(CardGroup::class.java)
        recyclerView.also {
            it.layoutManager = ScrollableLinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false,
                cardGroup.design_type == "HC9" || cardGroup.is_scrollable
            )
            it.setHasFixedSize(!cardGroup.is_scrollable)
            it.adapter = CardGroupAdapter(cardGroup, context)
        }
    }

    inner class DataViewHolder(val binding: RecyclerviewCardGroupBinding) : RecyclerView.ViewHolder(binding.root)

    inner class HC3CardGroupHolder(val binding: CardviewHc3Binding) : RecyclerView.ViewHolder(binding.root)

    inner class HC6CardGroupHolder(val binding: CardviewHc6Binding) : RecyclerView.ViewHolder(binding.root)

    /**
     * LinearLayoutManager with scrollable parameter
     *
     * @param scrollable Should the recyclerview scroll horizontally
     */
    inner class ScrollableLinearLayoutManager(context: Context,
                                              orientation: Int,
                                              reverseLayout: Boolean,
                                              private val scrollable: Boolean = true) : LinearLayoutManager(context, orientation, reverseLayout) {
        override fun canScrollHorizontally(): Boolean = scrollable && super.canScrollHorizontally()
    }
}