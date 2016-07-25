package com.thea.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * @author Thea (theazhu0321@gmail.com)
 */
public class FaceProgressBar extends View {
    private final static int DEFAULT_BAR_COLOR = 0xFF2196F3;
    private final static float DEFAULT_BAR_WIDTH = 6f;

    private int mBarColor;
    private float mBarWidth;
    private float mEyeWidth = 0f;
    private float mPadding = 0f;

    private Paint mPaint;

    private float mWidth = 0f;
    private float startAngle = 0f;

    private boolean isFinished = false;
    private boolean isSucceeded = false;

    public FaceProgressBar(Context context) {
        this(context, null);
    }

    public FaceProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FaceProgressBar);
        mBarColor = typedArray.getColor(R.styleable
                .FaceProgressBar_progressBarColor, DEFAULT_BAR_COLOR);
        mBarWidth = typedArray.getDimension(R.styleable.FaceProgressBar_progressBarWidth,
                DEFAULT_BAR_WIDTH);
        typedArray.recycle();

        mEyeWidth = mBarWidth * 3 / 2;
        mPadding = dip2px(10);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mBarColor);
        mPaint.setStrokeWidth(mBarWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (getMeasuredWidth() > getHeight())
            mWidth = getMeasuredHeight();
        else
            mWidth = getMeasuredWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rectF = new RectF(mPadding, mPadding, mWidth - mPadding, mWidth - mPadding);

        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(rectF, startAngle, 180, false, mPaint);//第四个参数是否显示半径

        mPaint.setStyle(Paint.Style.FILL);
        if (isFinished) {
            if (isSucceeded) {
                canvas.drawCircle(mPadding + mEyeWidth + mEyeWidth / 2, mWidth / 3, mEyeWidth,
                        mPaint);
                canvas.drawCircle(mWidth - mPadding - mEyeWidth - mEyeWidth / 2, mWidth / 3,
                        mEyeWidth, mPaint);
            } else {
                canvas.drawLine(mPadding + mEyeWidth * 2, mWidth / 3 - mEyeWidth, mPadding +
                        mEyeWidth * 4, mWidth / 3 + mEyeWidth, mPaint);
                canvas.drawLine(mPadding + mEyeWidth * 2, mWidth / 3 + mEyeWidth, mPadding +
                        mEyeWidth * 4, mWidth / 3 - mEyeWidth, mPaint);
                canvas.drawLine(mWidth - mPadding - mEyeWidth * 2, mWidth / 3 - mEyeWidth,
                        mWidth - mPadding - mEyeWidth * 4, mWidth / 3 + mEyeWidth, mPaint);
                canvas.drawLine(mWidth - mPadding - mEyeWidth * 2, mWidth / 3 + mEyeWidth,
                        mWidth - mPadding - mEyeWidth * 4, mWidth / 3 - mEyeWidth, mPaint);

            }
        }

        if (valueAnimator == null)
            startAnim();
    }

    public void finish(boolean succeeded) {
        isSucceeded = succeeded;
        stopAnim();
    }

    public void startAnim() {
        if (!isFinished)
            startViewAnim(0f, 1f, 2000);
    }

    private void stopAnim() {
        if (valueAnimator != null) {
            clearAnimation();
            isFinished = false;
            startAngle = 0f;
            valueAnimator.setRepeatCount(0);
            valueAnimator.cancel();
            valueAnimator.end();
        }
    }

    public int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    ValueAnimator valueAnimator;

    private ValueAnimator startViewAnim(float startF, final float endF, long time) {
        valueAnimator = ValueAnimator.ofFloat(startF, endF);
        valueAnimator.setDuration(time);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);//无限循环
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float mAnimatedValue = (float) valueAnimator.getAnimatedValue();
                if (isSucceeded) {
                    startAngle = 720;
                    isFinished = true;
                } else {
                    isFinished = false;
                    startAngle = 720 * mAnimatedValue;
                }
                invalidate();
            }
        });
        if (!valueAnimator.isRunning()) {
            valueAnimator.start();
        }

        return valueAnimator;
    }
}
