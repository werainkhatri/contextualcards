package com.werainkhatri.contextualcards.data.models

import androidx.lifecycle.ViewModel

class CardGroup(
        val id: Int,
        val name: String,
        val design_type: String,
        val is_scrollable: Boolean,
        val cards: List<Card>,
        val height: Int? = null
) : ViewModel()