package com.pyschool.app.ui.common

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import com.pyschool.app.R

/** วงแหวนแสดงเปอร์เซ็นต์คะแนน (วาดด้วย Canvas) เรียก [animateTo] เพื่อให้เส้นวิ่งจาก 0 ไปถึงค่าที่ต้องการ */
class ScoreRingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private val stroke = 16f * resources.displayMetrics.density
    private val rect = RectF()
    private val trackPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = stroke
        color = ContextCompat.getColor(context, R.color.line)
    }
    private val arcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = stroke
        strokeCap = Paint.Cap.ROUND
        color = ContextCompat.getColor(context, R.color.purple_deep)
    }
    private var fraction = 0f

    fun animateTo(target: Float) {
        ValueAnimator.ofFloat(0f, target.coerceIn(0f, 1f)).apply {
            duration = 900
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                fraction = it.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        val half = stroke / 2f
        rect.set(half, half, w - half, h - half)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawOval(rect, trackPaint)
        if (fraction > 0f) canvas.drawArc(rect, -90f, 360f * fraction, false, arcPaint)
    }
}
