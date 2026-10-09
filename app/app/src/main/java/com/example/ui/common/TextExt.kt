package com.pyschool.app.ui.common

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import androidx.core.content.ContextCompat
import com.pyschool.app.R

/** ทำให้คำหนึ่งในหัวข้อเป็นตัวเอียงพร้อมขีดเส้นใต้เหลืองหนา (สไตล์หัวข้อของ PySchool) */
fun highlightedTitle(context: Context, full: String, word: String): CharSequence {
    val start = full.indexOf(word)
    if (start < 0) return full
    val end = start + word.length
    val thickness = 7f * context.resources.displayMetrics.density
    return SpannableString(full).apply {
        setSpan(StyleSpan(Typeface.ITALIC), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        setSpan(
            ThickUnderlineSpan(ContextCompat.getColor(context, R.color.yellow), thickness),
            start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
        )
    }
}
