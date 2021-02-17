package com.werainkhatri.contextualcards.data.models

data class CallToAction(
        val text: String,
        val bg_color: String? = null,
        val url: String? = null,
        val text_color: String? = null, // TODO find a better data type than String
)