package com.werainkhatri.contextualcards.data.models

data class Gradient(
        val colors: List<String>, // TODO find a better data type than String
        val angle: Double? = null,
    )