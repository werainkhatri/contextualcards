package com.werainkhatri.contextualcards.ui.card_groups

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.werainkhatri.contextualcards.R
import com.werainkhatri.contextualcards.data.models.CardGroup
import com.werainkhatri.contextualcards.databinding.CardviewHc1Binding
import com.werainkhatri.contextualcards.databinding.RecyclerviewCardBinding
import com.werainkhatri.contextualcards.utils.Utils

class CardGroupAdapter(private val cardGroup: CardGroup, private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val tag = "CardGroupAdapter"

    private lateinit var rootView: View

    override fun getItemCount(): Int = cardGroup.cards.size

    override fun getItemViewType(position: Int): Int {
        return when(cardGroup.design_type) {
            "HC1" -> 1
            "HC3" -> 3
            "HC5" -> 5
            "HC6" -> 6
            else -> 9
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        rootView = parent.rootView
        return when(viewType) {
            1 -> HC1CardGroupHolder(
                    DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.cardview_hc1, parent, false)
            )
            else -> CardGroupHolder(
                    DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.recyclerview_card, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            1 -> bindHC1(holder, position)
            else -> {
                val mHolder = holder as CardGroupHolder
                mHolder.binding.card = cardGroup.cards[position]
            }
        }
    }

    private fun bindHC1(h: RecyclerView.ViewHolder, i: Int) {
        val card = cardGroup.cards[i]
        val holder = h as HC1CardGroupHolder
        val layout = holder.binding.parentLayout
        val params = layout.layoutParams as RecyclerView.LayoutParams
        holder.binding.card = card

        // Set bg_color to white if variable is null
        if(card.bg_color == null) card.bg_color = "#FFFFFF"

        // If not scrollable, fit all cards into the screen width
        if(!cardGroup.is_scrollable)
            layout.layoutParams.width = (rootView.width - Utils.px2dp(context,20)) / cardGroup.cards.size

        // first and last cards should have extra padding
        if(i == 0) {
            params.setMargins(Utils.px2dp(context, 10), 0, 0, 0)
        } else if(i == cardGroup.cards.size - 1) {
            params.setMargins(0, 0, Utils.px2dp(context, 10), 0)
        }

        // Remove description if null
        if(card.formatted_description == null)
            holder.binding.cardDescription.visibility = View.GONE

        holder.binding.cardView.elevation = 0F

        // loading to view image using glide
        Glide.with(rootView)
                .load(card.icon?.image_url)
                .apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions.overrideOf(130, 130))
                .into(holder.binding.icon)
    }

    inner class HC1CardGroupHolder(val binding: CardviewHc1Binding) : RecyclerView.ViewHolder(binding.root)

    inner class CardGroupHolder(val binding: RecyclerviewCardBinding) : RecyclerView.ViewHolder(binding.root)
}