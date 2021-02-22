package com.werainkhatri.contextualcards.utils

import android.content.Context
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.TranslateAnimation

object Utils {
    fun px2dp(context: Context, px: Int) : Int =
            (context.resources.displayMetrics.density * px).toInt()

    fun horizontalTranslation(start: Float, end: Float, duration: Long = 100): Animation {
        val outToRight: Animation = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, start,
            Animation.RELATIVE_TO_PARENT, end,
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f
        )
        outToRight.duration = duration
        outToRight.interpolator = AccelerateInterpolator()
        outToRight.fillAfter = true
        return outToRight
    }
}