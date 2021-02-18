package com.werainkhatri.contextualcards.ui.card_groups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.werainkhatri.contextualcards.data.models.CardGroup

@Suppress("UNCHECKED_CAST")
class CardGroupFactory(
    private val cardGroup: CardGroup
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T = cardGroup as T

}