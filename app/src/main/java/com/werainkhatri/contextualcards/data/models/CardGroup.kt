package com.werainkhatri.contextualcards.data.models

data class CardGroup(
        val name: String,
        val design_type: String,
        val is_scrollable: Boolean,
        val cards: List<Card>,
        val height: Int? = null
)