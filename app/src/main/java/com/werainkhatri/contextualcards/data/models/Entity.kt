package com.werainkhatri.contextualcards.data.models

data class Entity(
        val text: String,
        val color: String, // TODO verify the exact type and check if this can be of more specific type (like Color)
        val url: String,
        val font_style: String // TODO verify the exact type in api
)