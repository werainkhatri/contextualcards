package com.werainkhatri.contextualcards.data.models

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.*
import android.util.Log
import android.view.View
import com.werainkhatri.contextualcards.utils.ClickableWithoutUnderlineSpan
import java.lang.IllegalArgumentException

data class FormattedText(val text: String, val entities: List<Entity>) {
    private val tag = "FormattedText"

    fun string(fallback: String, context: Context) : SpannableStringBuilder {
        return try {
            structureString(context)
        } catch (e : Exception) {
            SpannableStringBuilder().append(fallback)
        }
    }

    private fun structureString(context: Context) : SpannableStringBuilder {
        val builder = SpannableStringBuilder()
        var i = 0 // index of last parsed character
        var ei = 0 // entity index
        val len = text.length-1
        for(j in 0..len) {
            if(text[j]=='{' && j+1 <= len && text[j+1] == '}') {
                if(ei >= entities.size) throw IllegalArgumentException("Number of entities are less than the number of \"{}\" brackets")
                builder.append(SpannableString(text.subSequence(i,j)))
                i=j+2
                val string = SpannableString(entities[ei].text)
                entities[ei].url?.let {
                    val span = object : ClickableWithoutUnderlineSpan() {
                        override fun onClick(p0: View) = context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
                    }
                    string.setSpan(span, 0, string.length, 0)
                }
                entities[ei].color?.let {
                    string.setSpan(ForegroundColorSpan(Color.parseColor(it)), 0, string.length, 0)
                }
                entities[ei].font_style?.let {
                    when (it) {
                        "bold" -> string.setSpan(StyleSpan(Typeface.BOLD), 0, string.length, 0)
                        "underline" -> string.setSpan(UnderlineSpan(), 0, string.length, 0)
                        else -> throw IllegalArgumentException("font_style contains neither \"bold\" nor \"underline\"")
                    }
                }
                builder.append(string)
                ei++
            }
        }
        if(ei != entities.size) throw throw IllegalArgumentException("Number of entities are more than the number of \"{}\" brackets")
        if(i<=len) builder.append(SpannableString(text.subSequence(i, text.length-1)))
        return builder
    }

}