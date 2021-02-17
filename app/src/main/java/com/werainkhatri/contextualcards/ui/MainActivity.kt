package com.werainkhatri.contextualcards.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.werainkhatri.contextualcards.R
import com.werainkhatri.contextualcards.data.network.APIResponse
import com.werainkhatri.contextualcards.data.repositories.DataRepository
import com.werainkhatri.contextualcards.utils.APIException
import com.werainkhatri.contextualcards.utils.Coroutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private val tag = "MainActivity"
    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Testing the API
        Coroutines.io {
            try {
                val response: APIResponse = DataRepository.getInstance().getData()
                response.card_groups?.let {
                    withContext(Dispatchers.Main) { Log.d(tag, "API Successful") }
                    return@io
                }
            } catch (e: APIException) {
                withContext(Dispatchers.Main) { Log.e(tag, "APIException: $e") }
            }
        }
    }
}