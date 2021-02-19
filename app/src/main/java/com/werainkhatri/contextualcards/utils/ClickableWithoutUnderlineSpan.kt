package com.werainkhatri.contextualcards.utils

import android.text.TextPaint
import android.text.style.ClickableSpan

abstract class ClickableWithoutUnderlineSpan : ClickableSpan() {
    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = false // set to false to remove underline
    }
}