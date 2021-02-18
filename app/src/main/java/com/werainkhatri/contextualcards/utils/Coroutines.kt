package com.werainkhatri.contextualcards.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object Coroutines {
    // TODO optimize by saving a launched coroutine
    fun<T: Any> ioThenMain(work: suspend () -> T?, success: (T?) -> Unit, failure: (Throwable?) -> Unit) = CoroutineScope(Dispatchers.IO).launch {
        val result = kotlin.runCatching { work() }
        withContext(Dispatchers.Main) {
            if(result.isSuccess) success(result.getOrNull())
            else failure(result.exceptionOrNull())
        }

    }
}