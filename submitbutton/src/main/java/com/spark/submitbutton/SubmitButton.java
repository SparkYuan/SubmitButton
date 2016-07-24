package com.spark.submitbutton;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

/**
 * Created by Spark on 2016/7/13 10:53.
 */
public class SubmitButton extends TextView {
    private static final String TAG = "SubmitButton";
    private float mRadius;
    private float mRecWidth;
    private float mLinePosX;
    private float mSweepAng = 180;
    private float mLineWidth;
    private float mLineRadius;
    private float mRippleWidth;
    private float mInitRippleWidth;
    private float mMaxRippleWidth;
    private float mSweepAngMin = 20f;
    private float mTickLen;
    private float cxLeft;
    private float cyLeft;
    private float cxRight;
    private float cyRight;
    private int mInitTextColor;
    private float[] mTickLeftStartPosition = new float[2];
    private float[] mTickLeftEndPosition = new float[2];
    private float[] mTickRightStartPosition = new float[2];
    private float[] mTickRightEndPosition = new float[2];
    private long mRippleDuration;
    private Paint mButtonPaint;
    private Paint mLinePaint;
    private Paint mRipplePaint;
    private Paint mTickPaint;
    private int mRippleAlpha;
    private String mStatus = "INIT";
    private final String INIT = "INIT";
    private final String RIPPLE = "RIPPLE";
    private final String LINE_MOVE = "LINE_MOVE";
    private final String TICK = "TICK";
    private ValueAnimator rippleAnimator;
    private ValueAnimator rippleAlphaAnimator;
    private ValueAnimator linePosXAnim;
    private ValueAnimator sweepAngAnim;
    private ValueAnimator tickRightEndAnim;
    private ValueAnimator tickRightStartAnim;
    private ValueAnimator tickLeftEndAnim;
    private ValueAnimator tickLeftStartAnim;
    private ValueAnimator btnBgColorAnim;
    private ValueAnimator tickColorAnim;
    private int mInitBtnColor;
    private int mLineColor;
    private int mTickColor;

    public SubmitButton(Context context) {
        super(context);
        init(null);
    }

    public SubmitButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SubmitButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        switch (mStatus) {
            case INIT:
                drawLine(canvas, 90, 180, 0);
                drawButton(canvas);
                break;
            case RIPPLE:
                drawRipple(canvas);
                drawLine(canvas, 90, 180, 0);
                drawButton(canvas);
                break;
            case LINE_MOVE:
                drawLine(canvas, 90, mSweepAng, mLinePosX);
                drawButton(canvas);
                break;
            case TICK:
                drawButton(canvas);
                drawTick(canvas);
                break;
        }
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP: {
                if (mStatus.equals(INIT)) {
                    startAnimation();
                } else {
                    mStatus = INIT;
                    rippleAnimator.cancel();
                    rippleAlphaAnimator.cancel();
                    linePosXAnim.cancel();
                    sweepAngAnim.cancel();
                    tickRightEndAnim.cancel();
                    tickRightStartAnim.cancel();
                    tickLeftEndAnim.cancel();
                    tickLeftStartAnim.cancel();
                    btnBgColorAnim.cancel();
                    tickColorAnim.cancel();
                    startAnimation();
                }
                break;
            }
        }
        super.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float textSize = getTextSize();
        float textLen = getText().length();
        mRadius = Math.round(2 * textSize);
        mTickLen = Math.round(mRadius / 180 * 3.14 * mSweepAngMin);
        mMaxRippleWidth = mRadius;
        mLineWidth = Math.round(mRadius / 10);
        mRippleWidth = mLineWidth;
        mInitRippleWidth = mRippleWidth;
        mRecWidth = textLen * textSize;
        float mWidth = mRecWidth + 2 * (mRadius + mLineWidth + mMaxRippleWidth);
        float mHeight = 2 * (mRadius + mLineWidth + mMaxRippleWidth);
        mLineRadius = mRadius + mLineWidth;
        cxLeft = mLineRadius + mMaxRippleWidth;
        cyLeft = mLineRadius + mMaxRippleWidth;
        cxRight = cxLeft + mRecWidth;
        cyRight = cyLeft;

        mLinePaint.setStrokeWidth(mLineWidth);
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mTickPaint.setStrokeWidth(mLineWidth);
        mTickPaint.setStrokeCap(Paint.Cap.ROUND);

        int width = MeasureSpec.makeMeasureSpec((int) mWidth, MeasureSpec.getMode(widthMeasureSpec));
        int height = MeasureSpec.makeMeasureSpec((int) mHeight, MeasureSpec.getMode(heightMeasureSpec));
        super.onMeasure(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        setGravity(Gravity.CENTER);
        super.onLayout(changed, left, top, right, bottom);
    }

    void init(AttributeSet attrs) {

        TypedArray typeArray = getContext().obtainStyledAttributes(attrs,
                R.styleable.SubmitButton);
        mButtonPaint = new Paint();
        mInitBtnColor = typeArray.getColor(R.styleable.SubmitButton_sub_btn_background,
                ContextCompat.getColor(getContext(), R.color.sub_btn_background));
        mButtonPaint.setColor(mInitBtnColor);
        mButtonPaint.setAntiAlias(true);
        mLinePaint = new Paint();
        mLineColor = typeArray.getColor(R.styleable.SubmitButton_sub_btn_line_color,
                ContextCompat.getColor(getContext(), R.color.sub_btn_line));
        mLinePaint.setColor(mLineColor);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setAntiAlias(true);
        mTickPaint = new Paint();
        mTickColor = typeArray.getColor(R.styleable.SubmitButton_sub_btn_tick_color,
                ContextCompat.getColor(getContext(), R.color.white));
        mTickPaint.setColor(mTickColor);
        mTickPaint.setStyle(Paint.Style.STROKE);
        mRipplePaint = new Paint();
        int mRippleColor = typeArray.getColor(R.styleable.SubmitButton_sub_btn_ripple_color,
                ContextCompat.getColor(getContext(), R.color.sub_btn_ripple));
        mRipplePaint.setColor(mRippleColor);
        mRipplePaint.setAntiAlias(true);
        mRippleDuration = typeArray.getInt(R.styleable.SubmitButton_sub_btn_duration,200) / 6;
        mInitTextColor = getCurrentTextColor();
        typeArray.recycle();
    }

    void drawButton(Canvas canvas) {

        if (mStatus.equals(TICK)) {
            setTextColor(Color.TRANSPARENT);
        } else {
            setTextColor(mInitTextColor);
            mButtonPaint.setColor(mInitBtnColor);
        }

        canvas.drawArc(new RectF(cxLeft - mRadius, cyLeft - mRadius, cxLeft + mRadius, cyLeft + mRadius),
                90, 360, false, mButtonPaint);
        canvas.drawRect(cxLeft, cyLeft - mRadius, cxRight, cyRight + mRadius, mButtonPaint);
        canvas.drawArc(new RectF(cxRight - mRadius, cyRight - mRadius, cxRight + mRadius, cyRight +
                mRadius), -90, 360, false, mButtonPaint);
    }

    void drawLine(Canvas canvas, float startAng, float sweepAng, float startPosX) {
        float radius = mLineRadius - mLineWidth / 2;
        canvas.drawArc(new RectF(cxLeft - radius, cyLeft - radius, cxLeft + radius,
                cyLeft + radius), -startAng, -sweepAng, false, mLinePaint);
        canvas.drawLine(cxLeft + startPosX, cyLeft - radius,
                cxRight, cyLeft - radius, mLinePaint);
        canvas.drawLine(cxLeft, cyLeft + radius,
                cxRight - startPosX, cyLeft + radius, mLinePaint);
        canvas.drawArc(new RectF(cxRight - radius, cyRight - radius, cxRight + radius,
                cyRight + radius), startAng, -sweepAng, false, mLinePaint);
    }

    void drawRipple(Canvas canvas) {
        //TODO precise problem
        mRipplePaint.setAlpha(mRippleAlpha);
        float mRippleRadius = mLineRadius + mRippleWidth;
        canvas.drawArc(new RectF(cxLeft - mRippleRadius, cyLeft - mRippleRadius,
                cxLeft + mRippleRadius, cyLeft + mRippleRadius), 90, 180, false, mRipplePaint);
        canvas.drawRect(cxLeft, cyLeft - mLineRadius - mRippleWidth,
                cxRight, cyRight + mLineRadius + mRippleWidth, mRipplePaint);
        canvas.drawArc(new RectF(cxRight - mRippleRadius, cyRight - mRippleRadius,
                cxRight + mRippleRadius, cyRight + mRippleRadius), -90, 180, false, mRipplePaint);
    }

    void drawTick(Canvas canvas) {
        canvas.drawLine(mTickRightStartPosition[0], mTickRightStartPosition[1], mTickRightEndPosition[0],
                mTickRightEndPosition[1], mTickPaint);
        canvas.drawLine(mTickLeftStartPosition[0], mTickLeftStartPosition[1], mTickLeftEndPosition[0],
                mTickLeftEndPosition[1], mTickPaint);
    }

    public void startAnimation() {
        startRippleAnim();
    }

    private void startRippleAnim() {
        mStatus = RIPPLE;
        rippleAnimator = ValueAnimator.ofFloat(mRippleWidth, mMaxRippleWidth);
        rippleAlphaAnimator = ValueAnimator.ofInt(255, 0);
        rippleAnimator.setDuration(mRippleDuration);
        rippleAlphaAnimator.setDuration(mRippleDuration);
        rippleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRippleWidth = (Float) animation.getAnimatedValue();
            }
        });
        rippleAlphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRippleAlpha = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        rippleAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mRippleWidth = mInitRippleWidth;
                mRippleAlpha = 255;
                startLineAnim();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mRippleWidth = mInitRippleWidth;
                mRippleAlpha = 255;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(rippleAnimator, rippleAlphaAnimator);
        animSet.start();
    }

    private void startLineAnim() {
        mStatus = LINE_MOVE;
        int duration = 1;
        // (cxRight - cxLeft) != mRecWidth. 极小的数字也会画出一个点
        linePosXAnim = ValueAnimator.ofFloat(0, cxRight - cxLeft);
        linePosXAnim.setDuration(duration * mRippleDuration);
        linePosXAnim.setInterpolator(new LinearInterpolator());
        linePosXAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mLinePosX = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        sweepAngAnim = ValueAnimator.ofFloat(180f, mSweepAngMin);
        sweepAngAnim.setDuration(duration * mRippleDuration);
        sweepAngAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mSweepAng = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        sweepAngAnim.setInterpolator(new LinearInterpolator());
        sweepAngAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mLinePosX = 0;
                mSweepAng = 180f;
                startTickAnim();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        AnimatorSet animSet = new AnimatorSet();
        animSet.playSequentially(linePosXAnim, sweepAngAnim);
        animSet.start();
    }

    public void startTickAnim() {
        mStatus = TICK;
        Path rightEndPath = new Path();
        rightEndPath.moveTo(cxLeft, cyLeft - mLineRadius);
        rightEndPath.cubicTo(cxRight + 2 * mLineRadius, cyRight - 2 * mLineRadius,
                cxRight + 2 * mLineRadius,
                cyRight + 2 * mLineRadius,
                (float) (cxLeft + mRecWidth * 0.48 -  1.5 * mTickLen),
                (float) (cyLeft - mRadius * 0.25));
        final PathMeasure mPathMeasureRightEnd = new PathMeasure(rightEndPath, false);
        Path rightStartPath = new Path();
        rightStartPath.moveTo(cxLeft, cyLeft - mLineRadius);
        rightStartPath.cubicTo(cxRight + 2 * mLineRadius,
                cyRight - 2 * mLineRadius,
                cxRight + 2 * mLineRadius,
                cyRight + 2 * mLineRadius,
                (float) (cxLeft + mRecWidth * 0.48),
                (float) (cyLeft + mTickLen * Math.cos(45)));
        final PathMeasure mPathMeasureRightStart = new PathMeasure(rightStartPath, false);
        Path leftEndPath = new Path();
        leftEndPath.moveTo(cxRight, cyRight + mLineRadius);
        leftEndPath.cubicTo(cxLeft - 2 * mLineRadius, cyRight - 2 * mLineRadius,
                cxLeft - 2 * mLineRadius,
                cyLeft + 2 * mLineRadius,
                (float) (cxLeft + mRecWidth * 0.48 + 2 * mTickLen),
                cyLeft - mTickLen);

        Path leftStartPath = new Path();
        leftStartPath.moveTo(cxRight, cyRight + mLineRadius);
        leftStartPath.cubicTo(cxLeft - 3 * mLineRadius, cyRight - 3 * mLineRadius,
                cxLeft - 3 * mLineRadius,
                cyLeft + 3 * mLineRadius,
                (float) (cxLeft + mRecWidth * 0.48),
                (float) (cyLeft + mTickLen * Math.cos(45)));

        final PathMeasure mPathMeasureLeftEnd = new PathMeasure(leftEndPath, false);
        final PathMeasure mPathMeasureLeftStart = new PathMeasure(leftStartPath, false);
        btnBgColorAnim = ValueAnimator.ofObject(new ArgbEvaluator(), mInitBtnColor
                , mLineColor);
        tickColorAnim = ValueAnimator.ofObject(new ArgbEvaluator(), mLineColor
                ,mTickColor);
        tickRightEndAnim = ValueAnimator.ofFloat(0, mPathMeasureRightEnd.getLength());
        tickRightStartAnim = ValueAnimator.ofFloat(0, mPathMeasureRightStart.getLength());
        tickLeftEndAnim = ValueAnimator.ofFloat(0, mPathMeasureLeftEnd.getLength());
        tickLeftStartAnim = ValueAnimator.ofFloat(0, mPathMeasureLeftStart.getLength());
        tickRightStartAnim.setInterpolator(new LinearInterpolator());
        int mTickDuration = 3;
        tickColorAnim.setDuration(mRippleDuration * mTickDuration);
        btnBgColorAnim.setDuration(mRippleDuration * mTickDuration);
        tickRightStartAnim.setDuration(mRippleDuration * mTickDuration);
        tickRightEndAnim.setDuration(mRippleDuration * mTickDuration);
        tickLeftEndAnim.setDuration(mRippleDuration * mTickDuration);
        tickLeftStartAnim.setDuration(mRippleDuration * mTickDuration);
        tickLeftStartAnim.setInterpolator(new LinearInterpolator());
        tickColorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mTickPaint.setColor((Integer) animation.getAnimatedValue());
            }
        });
        btnBgColorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mButtonPaint.setColor((Integer) animation.getAnimatedValue());
            }
        });
        tickLeftEndAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            float value;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                value = (Float) animation.getAnimatedValue();
                mPathMeasureLeftEnd.getPosTan(value, mTickLeftEndPosition, null);
            }
        });
        tickLeftStartAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            float value;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                value = (Float) animation.getAnimatedValue();
                mPathMeasureLeftStart.getPosTan(value, mTickLeftStartPosition, null);
            }
        });
        tickRightEndAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            float value;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                value = (Float) animation.getAnimatedValue();
                mPathMeasureRightEnd.getPosTan(value - mTickLen, mTickRightEndPosition, null);
                Log.d(TAG, "onAnimationUpdate: mTickRightEndPosition" + mTickRightEndPosition[0]);
                postInvalidate();
            }
        });
        tickRightStartAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            float value;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                value = (Float) animation.getAnimatedValue();
                mPathMeasureRightStart.getPosTan(value, mTickRightStartPosition, null);
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(tickLeftEndAnim, tickRightEndAnim, tickLeftStartAnim,
                tickRightStartAnim, btnBgColorAnim,tickColorAnim);
        animatorSet.start();
    }
}