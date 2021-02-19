package com.werainkhatri.contextualcards.data.models

data class Entity(
        val text: String,
        val color: String,
        val url: String,
        val font_style: String // TODO verify the exact type in api
)