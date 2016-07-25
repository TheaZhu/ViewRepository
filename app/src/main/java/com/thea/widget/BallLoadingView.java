package com.thea.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 * @author Thea (theazhu0321@gmail.com)
 */
public class BallLoadingView extends View {

    private int mDuration;
    private int mShadowColor;
    private int mBallRes;
    private Bitmap mBallBitmap;

    private int mRealWidth;
    private int mRealHeight;

    private ValueAnimator valueAnimator;
    private float mAnimatedValue = 0.f;

    private Paint mBallPaint, mShadowPaint;
    private RectF mShadowRectF = new RectF();

    public BallLoadingView(Context context) {
        this(context, null);
    }

    public BallLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BallLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable
                .BallLoadingView, defStyleAttr, R.style.Base_BallLoadingView);

        mDuration = typedArray.getInt(R.styleable.BallLoadingView_ballDuration, getResources()
                .getInteger(R.integer.ballDuration));
        mBallRes = typedArray.getResourceId(R.styleable.BallLoadingView_ballImage, R.mipmap
                .ic_ball);
        mShadowColor = typedArray.getColor(R.styleable.BallLoadingView_ballShadowColor,
                getResources().getColor(R.color.ballShadowColor));
        typedArray.recycle();

        mBallBitmap = BitmapFactory.decodeResource(getResources(), mBallRes);

        mBallPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShadowPaint.setStyle(Paint.Style.FILL);
        mShadowPaint.setColor(mShadowColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int usedWidth = getMeasuredWidth(widthMeasureSpec);
        int usedHeight = getMeasuredHeight(heightMeasureSpec);
        setMeasuredDimension(usedWidth, usedHeight);
    }

    public int getMeasuredWidth(int widthMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthVal = MeasureSpec.getSize(widthMeasureSpec);
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        if (widthMode == MeasureSpec.EXACTLY) {
            return paddingLeft + paddingRight + widthVal;
        } else if (widthMode == MeasureSpec.UNSPECIFIED) {
            return mBallBitmap.getWidth();
        } else {
            return Math.min(mBallBitmap.getWidth(), widthVal);
        }
    }

    public int getMeasuredHeight(int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightVal = MeasureSpec.getSize(heightMeasureSpec);
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        if (heightMode == MeasureSpec.EXACTLY) {
            return paddingTop + paddingBottom + heightVal;
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            return mBallBitmap.getHeight() * 3 / 2;
        } else {
            return Math.min(mBallBitmap.getHeight() * 3 / 2, heightVal);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRealWidth = w;
        mRealHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int ballWidth = mBallBitmap.getWidth();
        int ballHeight = mBallBitmap.getHeight();
        float left = (float) (mRealWidth - ballWidth) / 2;

        mShadowRectF.left = left + ballWidth * (1 - mAnimatedValue) / 4;
        mShadowRectF.right = left + ballWidth * (3 + mAnimatedValue) / 4;
        mShadowRectF.top = mRealHeight - ballHeight * (1 + mAnimatedValue) / 6;
        mShadowRectF.bottom = mRealHeight;

        canvas.drawArc(mShadowRectF, 0, 360, false, mShadowPaint);
        canvas.drawBitmap(mBallBitmap, left, (mRealHeight - ballHeight) * mAnimatedValue,
                mBallPaint);

        if(valueAnimator == null)
            start();
    }

    public void start() {
        startViewAnim(0f, 1f, mDuration);
    }

    public void stop() {
        if (valueAnimator != null) {
            clearAnimation();
            valueAnimator.setRepeatCount(0);
            valueAnimator.cancel();
            valueAnimator.end();
            mAnimatedValue = 0f;
            postInvalidate();
        }
    }

    private ValueAnimator startViewAnim(float startF, final float endF, long time) {
        valueAnimator = ValueAnimator.ofFloat(startF, endF);
        valueAnimator.setDuration(time);
        valueAnimator.setInterpolator(new AccelerateInterpolator(0.98f));
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);//无限循环
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                mAnimatedValue = (float) valueAnimator.getAnimatedValue();

                invalidate();
            }
        });
        if (!valueAnimator.isRunning()) {
            valueAnimator.start();
        }

        return valueAnimator;
    }
}
