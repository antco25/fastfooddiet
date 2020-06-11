package com.example.fastfooddiet.viewcomponent

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import com.example.fastfooddiet.R

class BarGraph(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var mWidth : Int = 0
    private var mHeight : Int = 0
    private var value : Float = 0f
    private var limit : Float = 1f
    private var valueRect : Rect = Rect()
    private var limitRect : Rect = Rect()
    private var limitEndX : Float = 0f
    private val displayMetrics : DisplayMetrics = context.resources.displayMetrics

    private val valuePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.detail_green)
    }

    private val limitPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.secondary_disabled_light)
    }

    private val limitEndPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = ContextCompat.getColor(context, R.color.secondary_light)
        strokeWidth = dpToPx(1f)
    }

    /*
    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.BarGraph,
            0, 0).apply {
            try {
                value = getInteger(R.styleable.BarGraph_values,0)
                limit = getInteger(R.styleable.BarGraph_limit,1)
            } finally {
                recycle()
            }
        }
    }

     */

    fun setValues(value : Float, limit : Float) {
        this.value = value
        this.limit = limit
        setDrawables()
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        mWidth = w
        mHeight = h

        setDrawables()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.apply {
            drawRect(limitRect,limitPaint)
            drawLine(limitEndX,0f,limitEndX,mHeight.toFloat(),limitEndPaint)
            drawRect(valueRect,valuePaint)
        }
    }

    private fun setDrawables() {
        val padding = (0.25f*mHeight.toFloat()).toInt()

        val percent = value / limit
        setValuePaint(percent)

        /*
        The width of the bar is fixed. When the value is less than the limit,
        the limit's width is the same as the bar and the value's width is a percentage of the bar.
        When the value is greater than the limit, the value's width is the same as the bar and
        the limit's width is a percentage of the bar
         */

        if (percent > 1) {

            valueRect = Rect(0,padding, mWidth,mHeight-padding)
            limitRect = Rect(0,padding,
                (mWidth / percent).toInt()-limitEndPaint.strokeWidth.toInt(),
                mHeight-padding)

            limitEndX = limitRect.right.toFloat() + limitEndPaint.strokeWidth/2

        } else {

            valueRect = Rect(0,padding, (mWidth * percent).toInt(),mHeight-padding)
            limitRect = Rect(0,padding, mWidth-limitEndPaint.strokeWidth.toInt(),
                mHeight-padding)

            limitEndX = limitRect.right.toFloat() + limitEndPaint.strokeWidth/2

        }
    }

    private fun setValuePaint(percent : Float) {
        valuePaint.color = when {
            percent < 0.4f -> ContextCompat.getColor(context, R.color.detail_green)
            percent < 0.8 -> ContextCompat.getColor(context, R.color.detail_yellow)
            else -> ContextCompat.getColor(context, R.color.detail_red)
        }
    }
    private fun dpToPx(dp : Float) : Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics)
    }
}