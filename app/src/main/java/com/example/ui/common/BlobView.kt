package com.pyschool.app.ui.common

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.pyschool.app.R

/**
 * รูปทรง "blob" สีเหลืองของโลโก้ Py (วาดด้วย Canvas)
 * พอร์ตตรงจาก CSS ของโปรโตไทป์:
 *   border-radius: 57% 43% 61% 39% / 43% 58% 42% 57%
 * วาด 3 ชั้น: เงาเลื่อน → สีพื้น → เส้นขอบ
 */
class BlobView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    private val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.STROKE }
    private val path = Path()
    private val bounds = RectF()
    private var shadowOffset = 0f

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.BlobView, defStyleAttr, 0)
        fillPaint.color = a.getColor(R.styleable.BlobView_blobFill, ContextCompat.getColor(context, R.color.yellow))
        strokePaint.color = a.getColor(R.styleable.BlobView_blobStroke, ContextCompat.getColor(context, R.color.ink))
        strokePaint.strokeWidth = a.getDimension(R.styleable.BlobView_blobStrokeWidth, 3f * resources.displayMetrics.density)
        shadowPaint.color = a.getColor(R.styleable.BlobView_blobShadowColor, 0x24202020)
        shadowOffset = a.getDimension(R.styleable.BlobView_blobShadowOffset, 0f)
        a.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        val inset = strokePaint.strokeWidth / 2f
        bounds.set(inset, inset, w - shadowOffset - inset, h - shadowOffset - inset)
        val bw = bounds.width()
        val bh = bounds.height()
        // (rx, ry) ของมุม: บนซ้าย, บนขวา, ล่างขวา, ล่างซ้าย
        val radii = floatArrayOf(
            bw * .57f, bh * .43f,
            bw * .43f, bh * .58f,
            bw * .61f, bh * .42f,
            bw * .39f, bh * .57f,
        )
        path.reset()
        path.addRoundRect(bounds, radii, Path.Direction.CW)
    }

    override fun onDraw(canvas: Canvas) {
        if (shadowOffset > 0f) {
            canvas.save()
            canvas.translate(shadowOffset, shadowOffset)
            canvas.drawPath(path, shadowPaint)
            canvas.restore()
        }
        canvas.drawPath(path, fillPaint)
        canvas.drawPath(path, strokePaint)
    }
}
