package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import com.udacity.ext.disableViewDuringAnimation
import com.udacity.ext.dp
import com.udacity.ext.sp
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private val textBounds = Rect()

    private var progress = 0f
    private var valueAnimator = ValueAnimator()

    private var bgColorDefaultState = 0
    private var bgColorLoadingState = 0
    private var loadingCircleColor = 0
    private var textColor = 0
    private var textSize: Float = 0.0f
    private var text: String = ""

    private var circleMarginRightOfText = 8f.dp()
    private var circleRadius = 12f.dp()

    private var textPaint: Paint
    private var loadingBgPaint: Paint
    private var defaultBgPaint: Paint
    private var loadingArcPaint: Paint

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Loading -> {
                startLoadingAnimRepeatedly(2000L, 0f, 1f)
                text = resources.getString(R.string.button_loading_text)
                invalidate()
            }
            ButtonState.Completed -> {
                stopAnim()
                text = resources.getString(R.string.button_default_text)
                invalidate()
            }
            else -> {

            }
        }
    }

    init {
        isClickable = true

        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            bgColorDefaultState = getColor(
                R.styleable.LoadingButton_bgColorDefaultState,
                resources.getColor(R.color.colorPrimary)
            )
            bgColorLoadingState = getColor(
                R.styleable.LoadingButton_bgColorLoadingState,
                resources.getColor(R.color.colorPrimaryDark)
            )
            loadingCircleColor = getColor(
                R.styleable.LoadingButton_circleColor,
                resources.getColor(R.color.colorAccent)
            )
            textColor = getColor(
                R.styleable.LoadingButton_android_textColor,
                resources.getColor(R.color.white)
            )
            textSize = getDimension(R.styleable.LoadingButton_android_textSize, 14f.sp())
        }

        buttonState = ButtonState.Completed

        textPaint = Paint(Paint.ANTI_ALIAS_FLAG).also {
            it.style = Paint.Style.FILL
            it.textAlign = Paint.Align.CENTER
            it.textSize = textSize
            it.color = textColor
        }

        loadingBgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = bgColorLoadingState
        }

        defaultBgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = bgColorDefaultState
        }

        loadingArcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = loadingCircleColor
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Measure text bounds
        textPaint.getTextBounds(text, 0, text.length, textBounds)

        // Draw default background
        canvas.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), defaultBgPaint)

        if (buttonState == ButtonState.Loading) {
            // Draw loading background
            canvas.drawRect(
                0f,
                0f,
                progress * widthSize.toFloat(),
                heightSize.toFloat(),
                loadingBgPaint
            )

            // Draw arc
            val circleLeft = widthSize / 2f + textBounds.width() / 2f + circleMarginRightOfText
            val circleTop = heightSize / 2f - circleRadius
            val degree = progress * 360f
            canvas.drawArc(
                circleLeft,
                circleTop,
                circleLeft + circleRadius * 2f,
                circleTop + circleRadius * 2f,
                0f,
                degree,
                true,
                loadingArcPaint
            )
        }

        // Draw Text in Center
        val textX = widthSize.toFloat() / 2
        val textY = heightSize.toFloat() / 2 - textBounds.centerY()
        canvas.drawText(text, textX, textY, textPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    fun updateButtonState(state: ButtonState) {
        buttonState = state
    }

    private fun startLoadingAnimRepeatedly(durationInMillis: Long, start: Float, end: Float) {
        stopAnim()
        valueAnimator = ValueAnimator.ofFloat(start, end).also { animator ->
            animator.repeatCount = ValueAnimator.INFINITE
            animator.repeatMode = ValueAnimator.REVERSE
            animator.duration = durationInMillis
            animator.addUpdateListener {
                progress = it.animatedValue as Float
                invalidate()
            }
            animator.disableViewDuringAnimation(this)
            animator.start()
        }
    }

    private fun stopAnim() {
        if (valueAnimator.isRunning) {
            valueAnimator.cancel()
        }
        progress = 0f
    }
}