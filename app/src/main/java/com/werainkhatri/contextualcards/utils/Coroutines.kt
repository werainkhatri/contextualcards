package com.werainkhatri.contextualcards.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Coroutines {
    fun main(work: suspend(() -> Unit)) = CoroutineScope(Dispatchers.Main).launch { work() }

    // TODO optimize by saving a launched coroutine
    fun io(work: suspend () -> Unit) = CoroutineScope(Dispatchers.IO).launch { work() }
}