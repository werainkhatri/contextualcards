package com.werainkhatri.contextualcards.utils

import android.content.Context

object Utils {
    fun px2dp(context: Context, px: Int) : Int =
            (context.resources.displayMetrics.density * px).toInt()
}