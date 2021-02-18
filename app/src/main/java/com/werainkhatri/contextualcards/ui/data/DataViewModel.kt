package com.werainkhatri.contextualcards.ui.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.werainkhatri.contextualcards.data.models.CardGroup
import com.werainkhatri.contextualcards.data.repositories.DataRepository
import com.werainkhatri.contextualcards.utils.Coroutines
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job

class DataViewModel(
    private val repository: DataRepository
) : ViewModel() {
    private val tag = "DataViewModel"

    private lateinit var job: Job

    private val _data = MutableLiveData<List<CardGroup>>()
    val data: LiveData<List<CardGroup>>
    get() = _data

    @InternalCoroutinesApi
    fun updateData() {
        job = Coroutines.ioThenMain(
                work = { repository.getData() },
                success = {
                    if (it == null) Log.e(tag, "null APIResponse object")
                    else if (it.card_groups == null) Log.e(tag, "null card_groups field")
                    it?.let { apiResponse ->
                         apiResponse.card_groups?.let { cardGroups ->
                            _data.value = cardGroups
                        }
                    }
                },
                failure = { Log.e(tag, "APIException: ${it.toString()}") }
        )
    }

    override fun onCleared() {
        super.onCleared()
        if(::job.isInitialized) job.cancel()
    }
}