package com.werainkhatri.contextualcards.data.repositories

import com.werainkhatri.contextualcards.data.network.API
import com.werainkhatri.contextualcards.data.network.APIResponse
import com.werainkhatri.contextualcards.data.network.SafeAPIRequest
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

class DataRepository private constructor() : SafeAPIRequest() {

    suspend fun getData() : APIResponse = apiRequest { API().getData() }

    companion object {
        @Volatile private var instance: DataRepository? = null

        @InternalCoroutinesApi
        fun getInstance() =
                instance ?: synchronized(this) {
                    instance ?: DataRepository().also { instance = it }
                }
    }
}