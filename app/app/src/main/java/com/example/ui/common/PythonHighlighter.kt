package com.pyschool.app.ui.common

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import com.pyschool.app.R

/**
 * ไฮไลต์ไวยากรณ์ Python แบบเบา (ใช้ซ้ำใน Lesson / Visual / Quiz)
 * ใช้ regex ตัวเดียวแบบ alternation เพื่อให้แต่ละช่วงตัวอักษรถูกจับแค่ครั้งเดียว ไม่ซ้อนกัน
 * (comment และ string จึงไม่โดน keyword ทับ)
 */
object PythonHighlighter {

    private val KEYWORDS = listOf(
        "def", "return", "if", "elif", "else", "for", "while", "in", "and", "or", "not",
        "break", "continue", "True", "False", "None", "import", "pass",
    ).joinToString("|")

    private val BUILTINS = listOf("print", "range", "len", "input", "int", "float", "str", "bool", "type")
        .joinToString("|")

    // กลุ่ม: 1 comment, 2 string, 3 number, 4 keyword, 5 builtin
    private val TOKEN = Regex(
        """(#.*)|("[^"\n]*"|'[^'\n]*')|\b(\d+(?:\.\d+)?)\b|\b($KEYWORDS)\b|\b($BUILTINS)\b"""
    )

    fun highlight(context: Context, code: String): CharSequence {
        val colors = intArrayOf(
            ContextCompat.getColor(context, R.color.code_comment),
            ContextCompat.getColor(context, R.color.yellow),
            ContextCompat.getColor(context, R.color.code_number),
            ContextCompat.getColor(context, R.color.purple),
            ContextCompat.getColor(context, R.color.code_builtin),
        )
        val result = SpannableString(code)
        TOKEN.findAll(code).forEach { match ->
            // groupValues[0] คือทั้ง match; หา group แรกที่มีค่า
            val index = (1..5).first { match.groups[it] != null }
            result.setSpan(
                ForegroundColorSpan(colors[index - 1]),
                match.range.first, match.range.last + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
        }
        return result
    }
}
