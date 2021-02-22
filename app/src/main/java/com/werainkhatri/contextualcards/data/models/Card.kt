package com.werainkhatri.contextualcards.data.models


data class Card(
    val name: String,
    val formatted_title: FormattedText? = null,
    val title: String? = null,
    val formatted_description: FormattedText? = null,
    val description: String? = null,
    val icon: CardImage? = null,
    val url: String? = null,
    val bg_image: CardImage? = null,
    var bg_color: String? = null,
    val bg_gradient: Gradient? = null,
    val cta: List<CallToAction>? = null
)