package org.xhanka.ndm_project.ui.report_emergency.report

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import org.xhanka.ndm_project.R
import kotlin.math.pow
import kotlin.math.sqrt

class TapHoldUpButton : View {
    private val mHandler = Handler(Looper.getMainLooper())
    private var mRingPaint: Paint? = null
    private var mCirclePaint: Paint? = null
    private var mCircleGap = 0f
    private var mCircleColor = 0
    private var mCircleColorOnHold = 0
    private var longHoldEnabled = true
    private var mScalePercentage = 1f
    private var longHold = false
    private var touchState = 0
    private var downAnim: ValueAnimator? = null
    private var upAnim: ValueAnimator? = null
    private val mArgbEvaluator = ArgbEvaluator()
    private var mClickListener: OnButtonClickListener? = null

    interface OnButtonClickListener {
        fun onLongHoldStart(v: View?)
        fun onLongHoldEnd(v: View?)
        fun onClick(v: View?)
    }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    fun setOnButtonClickListener(listener: OnButtonClickListener?) {
        mClickListener = listener
    }

    fun enableLongHold(enable: Boolean) {
        longHoldEnabled = enable
    }

    fun resetLongHold() {
        if (!longHold) return
        touchState = -1
        startUpAnimation()
        endLongHold()
    }

    override fun onDraw(canvas: Canvas) {
        val halfWidth = width / 2f
        val halfHeight = height / 2f
        canvas.drawCircle(
            halfWidth,
            halfHeight,
            halfWidth - mRingPaint!!.strokeWidth / 2f,
            mRingPaint!!
        )
        canvas.drawCircle(
            halfWidth,
            halfHeight,
            (halfWidth - mRingPaint!!.strokeWidth - mCircleGap) * mScalePercentage,
            mCirclePaint!!
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (isInCircle(event.x, event.y)) {
                touchState = MotionEvent.ACTION_DOWN
                mHandler.postDelayed(Runnable {
                    if (!longHoldEnabled) return@Runnable
                    if (touchState == MotionEvent.ACTION_DOWN) {
                        longHold = true
                        if (mClickListener != null) mClickListener!!.onLongHoldStart(this@TapHoldUpButton)
                        startColorChangeAnimation(mCircleColor, mCircleColorOnHold)
                    }
                }, LONG_HOLD_DELAY.toLong())
                startDownAnimation()
            }
            return true
        } else if (event.action == MotionEvent.ACTION_UP && touchState == MotionEvent.ACTION_DOWN) {
            touchState = MotionEvent.ACTION_UP
            startUpAnimation()
            mHandler.removeCallbacksAndMessages(null)
            if (longHold) endLongHold()
            else mClickListener?.onClick(this@TapHoldUpButton)
            return true
        }
        return super.onTouchEvent(event)
    }

    private fun endLongHold() {
        longHold = false
        startColorChangeAnimation(mCircleColorOnHold, mCircleColor)
        mClickListener?.onLongHoldEnd(this@TapHoldUpButton)
    }

    private fun startColorChangeAnimation(startColor: Int, endColor: Int) {
        val anim = ValueAnimator.ofFloat(0f, 1f)
        anim.duration = 150
        anim.addUpdateListener { animation ->
            val col = mArgbEvaluator.evaluate(
                animation.animatedValue as Float,
                startColor,
                endColor
            ) as Int
            mCirclePaint!!.color = col
            invalidate()
        }
        anim.start()
    }

    private fun startDownAnimation() {
        if (downAnim != null) return
        downAnim = ValueAnimator.ofFloat(1f, .8f)
        downAnim?.duration = ANIM_DURATION.toLong()
        downAnim?.interpolator = DecelerateInterpolator()
        downAnim?.addUpdateListener {
            mScalePercentage = downAnim?.animatedValue as Float
            invalidate()
        }
        downAnim?.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                downAnim = null
                if (touchState == MotionEvent.ACTION_UP) {
                    startUpAnimation()
                }
            }
        })
        downAnim?.start()
    }

    private fun startUpAnimation() {
        if (upAnim != null || downAnim != null) return
        upAnim = ValueAnimator.ofFloat(.8f, 1f)
        upAnim?.duration = ANIM_DURATION.toLong()
        upAnim?.interpolator = DecelerateInterpolator()
        upAnim?.addUpdateListener {
            mScalePercentage = upAnim?.animatedValue as Float
            invalidate()
        }
        upAnim?.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                upAnim = null
            }
        })
        upAnim?.start()
    }

    private fun isInCircle(x: Float, y: Float): Boolean {
        // find the distance between center of the view and x,y point
        val distance = sqrt(
            (width / 2f - x).toDouble().pow(2.0) + (height / 2f - y).toDouble().pow(2.0)
        )
        return distance <= width / 2
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        mRingPaint = Paint()
        mRingPaint!!.style = Paint.Style.STROKE
        mRingPaint!!.isAntiAlias = true
        mCirclePaint = Paint()
        mCirclePaint!!.isAntiAlias = true
        if (attrs != null) {
            val arr = context.obtainStyledAttributes(attrs, R.styleable.TapHoldUpButton, 0, 0)
            mRingPaint!!.strokeWidth = arr.getDimensionPixelSize(
                R.styleable.TapHoldUpButton_thub_ringStrokeWidth,
                DEF_STROKE_WIDTH
            ).toFloat()
            mRingPaint!!.color = arr.getColor(
                R.styleable.TapHoldUpButton_thub_ringColor,
                DEF_RING_COLOR
            )
            mCirclePaint!!.color = arr.getColor(
                R.styleable.TapHoldUpButton_thub_circleColor,
                DEF_CIRCLE_COLOR
            )
            mCircleGap = arr.getDimensionPixelSize(
                R.styleable.TapHoldUpButton_thub_circleGap,
                DEF_CIRCLE_GAP
            ).toFloat()
            mCircleColor = mCirclePaint!!.color
            mCircleColorOnHold =
                arr.getColor(R.styleable.TapHoldUpButton_thub_circleColorOhHold, Color.RED)
            arr.recycle()
        } else {
            mRingPaint!!.strokeWidth = DEF_STROKE_WIDTH.toFloat()
            mRingPaint!!.color = DEF_RING_COLOR
            mCirclePaint!!.color = DEF_CIRCLE_COLOR
            mCircleColor = DEF_CIRCLE_COLOR
            mCircleGap = DEF_CIRCLE_GAP.toFloat()
            mCircleColorOnHold = Color.RED
        }
    }

    companion object {
        private const val DEF_STROKE_WIDTH = 3
        private const val DEF_RING_COLOR = Color.WHITE
        private const val DEF_CIRCLE_COLOR = Color.RED
        private const val DEF_CIRCLE_GAP = 0
        private const val LONG_HOLD_DELAY = 100
        private const val ANIM_DURATION = 150
    }
}