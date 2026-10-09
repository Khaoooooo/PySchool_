package com.pyschool.app.ui.common

import android.graphics.Canvas
import android.graphics.Paint
import android.text.style.ReplacementSpan

/** ขีดเส้นใต้หนาสีเหลืองหลังคำ (เช่นคำว่า "fun") — UnderlineSpan มาตรฐานเปลี่ยนสี/ความหนาไม่ได้ใน API < 29 */
class ThickUnderlineSpan(
    private val color: Int,
    private val thickness: Float,
) : ReplacementSpan() {

    override fun getSize(paint: Paint, text: CharSequence, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int =
        paint.measureText(text, start, end).toInt()

    override fun draw(
        canvas: Canvas, text: CharSequence, start: Int, end: Int,
        x: Float, top: Int, y: Int, bottom: Int, paint: Paint,
    ) {
        val width = paint.measureText(text, start, end)
        val line = Paint(paint).apply { this.color = this@ThickUnderlineSpan.color }
        val lineBottom = y + paint.descent() * .35f
        canvas.drawRoundRect(x, lineBottom - thickness, x + width, lineBottom, thickness / 2, thickness / 2, line)
        canvas.drawText(text, start, end, x, y.toFloat(), paint)
    }
}
