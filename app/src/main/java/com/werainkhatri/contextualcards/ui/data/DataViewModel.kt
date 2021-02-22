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

    fun removeAt(i: Int) {
        val list = _data.value?.toMutableList()
        list?.let {
            it.removeAt(i)
            _data.value = it.toList()
        }
    }

    @InternalCoroutinesApi
    fun updateData(toSkip: Set<Int>, onDone: () -> Unit) {
        job = Coroutines.ioThenMain(
            work = { repository.getData() },
            success = { response ->
                if (response == null) Log.e(tag, "null APIResponse object")
                else if (response.card_groups == null) Log.e(tag, "null card_groups field")
                response?.let { apiResponse ->
                    apiResponse.card_groups?.let { cardGroups ->
                        cardGroups.forEach {
                            if (toSkip.contains(it.id)) {
                                cardGroups.remove(it)
                            }
                        }
                        _data.value = cardGroups
                    }
                }
                onDone()
            },
            failure = {
                Log.e(tag, "APIException: ${it.toString()}")
                onDone()
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        if (::job.isInitialized) job.cancel()
    }
}