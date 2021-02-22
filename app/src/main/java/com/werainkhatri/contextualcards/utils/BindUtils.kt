package com.werainkhatri.contextualcards.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.werainkhatri.contextualcards.data.models.CardGroup
import com.werainkhatri.contextualcards.ui.card_groups.CardGroupAdapter
import com.werainkhatri.contextualcards.ui.data.DataAdapter

object BindUtils {
    private val tag = "BindUtils"

    /**
     * Util function to bind HC1
     */
    fun bindHC1(
            holder: CardGroupAdapter.HC1CardGroupHolder,
            i: Int,
            cardGroup: CardGroup,
            rootView: View,
            context: Context
    ) {
        val card = cardGroup.cards[i]
        val layout = holder.binding.parentLayout
        val params = layout.layoutParams as RecyclerView.LayoutParams
        holder.binding.card = card


        // If not scrollable, fit all cards into the screen width
        if (!cardGroup.is_scrollable)
            layout.layoutParams.width = (rootView.width - Utils.px2dp(context, 20)) / cardGroup.cards.size

        // first and last cards should have extra padding
        if (i == 0) {
            params.setMargins(Utils.px2dp(context, 10), 0, 0, 0)
        } else if (i == cardGroup.cards.size - 1) {
            params.setMargins(0, 0, Utils.px2dp(context, 10), 0)
        }

        // Remove description if null
        if (card.formatted_description == null || card.description == null) {
            holder.binding.cardDescription.visibility = View.GONE
        } else {
            // make text clickable
            holder.binding.cardDescription.movementMethod = LinkMovementMethod.getInstance()
            holder.binding.cardDescription.text = card.formatted_description.string(card.description, context)
        }

        // Remove title if null
        if (card.formatted_title == null || card.title == null) {
            holder.binding.cardTitle.visibility = View.GONE
        } else {
            // Make text clickable
            holder.binding.cardTitle.movementMethod = LinkMovementMethod.getInstance()
            holder.binding.cardTitle.text = card.formatted_title.string(card.title, context)
        }

        // removing elevation from card view, as required by design
        holder.binding.cardView.elevation = 0F

        // loading icon to view using glide. If null, removing it
        if (card.icon == null) {
            holder.binding.icon.visibility = View.GONE
        } else {
            Glide.with(rootView)
                    .load(card.icon.image_url)
                    .apply(RequestOptions.circleCropTransform())
                    .apply(RequestOptions.overrideOf(130, 130))
                    .into(holder.binding.icon)
        }

        // Set url for when user clicks on the card
        if (card.url != null)
            layout.setOnClickListener { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(card.url))) }

        // Set bg_color to white if variable is null
        if (card.bg_color == null) card.bg_color = "#FFFFFF"
        holder.binding.cardView.setCardBackgroundColor(Color.parseColor(card.bg_color))
    }

    fun bindHC3(
            holder: DataAdapter.HC3CardGroupHolder,
            i: Int,
            cardGroups: List<CardGroup>,
            rootView: View,
            context: Context,
            dismiss: (remind: Boolean) -> Unit
    ) {
        val card = cardGroups[i].cards[0]
        holder.binding.card = card
        if (card.bg_image != null)
            Glide.with(rootView)
                    .asBitmap()
                    .load(card.bg_image.image_url)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            val ratio = resource.width.toDouble() / resource.height.toDouble()
                            val screenWidth = 1440.toDouble()
                            holder.binding.bgImage.setImageBitmap(Bitmap.createScaledBitmap(
                                resource,
                                screenWidth.toInt(),
                                (screenWidth / ratio).toInt(),
                                false
                            ))
                        }
                        override fun onLoadCleared(placeholder: Drawable?) {}
                    })

        holder.binding.title.text = card.formatted_title?.string(card.title!!, context)
        holder.binding.description.text = card.formatted_description?.string(card.description!!, context)
        holder.binding.cta.setCardBackgroundColor(Color.parseColor(card.cta?.get(0)?.bg_color))
        holder.binding.cta.setOnClickListener {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(card.cta?.get(0)?.url)))
        }
        holder.binding.ctaText.text = card.cta?.get(0)?.text
        holder.binding.ctaText.setTextColor(Color.parseColor(card.cta?.get(0)?.text_color))

        holder.binding.remindLaterCard.elevation = 0.0f
        holder.binding.remindLaterCard.setOnClickListener { dismiss(true) }
        holder.binding.dismissCard.elevation = 0.0f
        holder.binding.dismissCard.setOnClickListener { dismiss(false) }

        var cardState = false
        holder.binding.bgImage.let { img ->
            img.setOnClickListener {
                if(it.animation != null && cardState && it.animation.hasEnded()) {
                    it.startAnimation(Utils.horizontalTranslation(+0.4f, 0.0f))
                    holder.binding.foreground.startAnimation(Utils.horizontalTranslation(+0.4f, 0.0f))
                    it.animation.setAnimationListener(object: Animation.AnimationListener {
                        override fun onAnimationStart(p0: Animation?) {
                            holder.binding.remindLaterCard.elevation = 0f
                            holder.binding.dismissCard.elevation = 0f
                        }
                        override fun onAnimationEnd(p0: Animation?) { cardState = !cardState }
                        override fun onAnimationRepeat(p0: Animation?) {}
                    })
                }
            }
            img.setOnLongClickListener {
                when {
                    it.animation == null -> {
                        it.startAnimation(Utils.horizontalTranslation(0.0f, +0.4f))
                        holder.binding.foreground.startAnimation(Utils.horizontalTranslation(0.0f, +0.4f))
                        it.animation.setAnimationListener(object: Animation.AnimationListener {
                            override fun onAnimationStart(p0: Animation?) {}
                            override fun onAnimationEnd(p0: Animation?) {
                                holder.binding.remindLaterCard.elevation = 1f
                                holder.binding.dismissCard.elevation = 1f
                                cardState = !cardState
                            }
                            override fun onAnimationRepeat(p0: Animation?) {}
                        })
                        true
                    }
                    it.animation.hasEnded() && !cardState -> {
                        it.startAnimation(Utils.horizontalTranslation(0.0f, +0.4f))
                        holder.binding.foreground.startAnimation(Utils.horizontalTranslation(0.0f, +0.4f))
                        it.animation.setAnimationListener(object: Animation.AnimationListener {
                            override fun onAnimationStart(p0: Animation?) {}
                            override fun onAnimationEnd(p0: Animation?) {
                                holder.binding.remindLaterCard.elevation = 1f
                                holder.binding.dismissCard.elevation = 1f
                                cardState = !cardState
                            }
                            override fun onAnimationRepeat(p0: Animation?) {}
                        })
                        true
                    }
                    else -> true
                }
            }
        }
    }

    /**
     * Util function to bind HC9
     */
    fun bindHC9(
            holder: CardGroupAdapter.HC9CardGroupHolder,
            i: Int,
            cardGroup: CardGroup,
            rootView: View,
            context: Context
    ) {
        val card = cardGroup.cards[i]
        val layout = holder.binding.parentLayout
        val params = layout.layoutParams as RecyclerView.LayoutParams

        // first and last cards should have extra padding
        if (i == 0) {
            params.setMargins(Utils.px2dp(context, 10), 0, 0, 0)
        } else if (i == cardGroup.cards.size - 1) {
            params.setMargins(0, 0, Utils.px2dp(context, 10), 0)
        }

        // loading background image to view using glide. If null, removing it
        if (card.bg_image == null) {
            holder.binding.parentLayout.visibility = View.GONE
        } else {
            Glide.with(rootView)
                    .asBitmap()
                    .load(card.bg_image.image_url)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            val ratio = resource.width.toDouble() / resource.height.toDouble()
                            holder.binding.bgImage.setImageBitmap(
                                    Bitmap.createScaledBitmap(
                                            resource,
                                            (ratio * Utils.px2dp(context, cardGroup.height!!)).toInt(),
                                            Utils.px2dp(context, cardGroup.height),
                                            false
                                    )
                            )
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                            TODO("Not yet implemented")
                        }
                    })

            card.url?.let { url ->
                holder.binding.bgImage.setOnClickListener {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                }
            }
        }
    }

}