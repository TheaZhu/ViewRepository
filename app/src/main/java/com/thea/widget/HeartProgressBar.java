package com.thea.widget;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * @author Thea (theazhu0321@gmail.com)
 */
public class HeartProgressBar extends ProgressBar {
    private final static int DEFAULT_UNREACHED_COLOR = 0xFF8A80;
    private final static int DEFAULT_REACHED_COLOR = 0xD50000;
    private final static int DEFAULT_INNER_TEXT_COLOR = 0xffffff;
    private final static int DEFAULT_INNER_TEXT_SIZE = 10;

    private int mUnReachedColor;
    private int mReachedColor;
    private int mInnerTextColor;
    private int mInnerTextSize;

    private Paint mUnderPaint;
    private Paint mTextPaint;
    private Path mPath;
    private int mRealWidth;
    private int mRealHeight;

    private ArgbEvaluator mArgbEvaluator;

    public HeartProgressBar(Context context) {
        this(context, null);
    }

    public HeartProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeartProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HeartProgressBar,
                defStyleAttr, R.style.Widget_HeartProgressBar);
        mUnReachedColor = typedArray.getColor(R.styleable.HeartProgressBar_unReachedColor,
                DEFAULT_UNREACHED_COLOR);
        mReachedColor = typedArray.getColor(R.styleable.HeartProgressBar_reachedColor,
                DEFAULT_REACHED_COLOR);
        mInnerTextColor = typedArray.getColor(R.styleable.HeartProgressBar_innerTextColor,
                DEFAULT_INNER_TEXT_COLOR);
        mInnerTextSize = typedArray.getDimensionPixelSize(R.styleable
                .HeartProgressBar_innerTextSize, DEFAULT_INNER_TEXT_SIZE);
        typedArray.recycle();

        mArgbEvaluator = new ArgbEvaluator();

        mUnderPaint = new Paint();
        mUnderPaint.setStyle(Paint.Style.FILL);
        mUnderPaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setColor(mInnerTextColor);
        mTextPaint.setTextSize(mInnerTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mPath = new Path();
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int usedWidth = getMeasuredWidth(widthMeasureSpec);
        int usedHeight = getMeasuredHeight(heightMeasureSpec);
        setMeasuredDimension(usedWidth, usedHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRealWidth = w;
        mRealHeight = h;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float pro = ((float) getProgress()) / 100.0f;
        int nowColor = (int) mArgbEvaluator.evaluate(pro, mUnReachedColor, mReachedColor);
        mUnderPaint.setColor(nowColor);
        mPath.moveTo((float) (0.5 * mRealWidth), (float) (0.17 * mRealHeight));
        mPath.cubicTo((float) (0.15 * mRealWidth), (float) (-0.35 * mRealHeight), (float) (-0.4 *
                mRealWidth), (float) (0.45 * mRealHeight), (float) (0.5 * mRealWidth), mRealHeight);
        mPath.moveTo((float) (0.5 * mRealWidth), mRealHeight);
        mPath.cubicTo((float) (mRealWidth + 0.4 * mRealWidth), (float) (0.45 * mRealHeight), (float)
                (mRealWidth - 0.15 * mRealWidth), (float) (-0.35 * mRealHeight), (float) (0.5 *
                mRealWidth), (float) (0.17 * mRealHeight));
        mPath.close();
        canvas.drawPath(mPath, mUnderPaint);
        canvas.drawText(String.valueOf(getProgress()), mRealWidth / 2, mRealHeight / 2, mTextPaint);
    }

    public int getMeasuredWidth(int widthMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthVal = MeasureSpec.getSize(widthMeasureSpec);
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        if (widthMode == MeasureSpec.EXACTLY) {
            return paddingLeft + paddingRight + widthVal;
        } else if (widthMode == MeasureSpec.UNSPECIFIED) {
            return (int) (Math.abs(mUnderPaint.ascent() - mUnderPaint.descent()) + paddingLeft +
                    paddingRight);
        } else {
            return (int) Math.min((Math.abs(mUnderPaint.ascent() - mUnderPaint.descent()) +
                    paddingLeft + paddingRight), widthVal);
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
            return (int) (Math.abs(mUnderPaint.ascent() - mUnderPaint.descent()) + paddingTop +
                    paddingBottom);
        } else {
            return (int) Math.min((Math.abs(mUnderPaint.ascent() - mUnderPaint.descent()) +
                    paddingTop + paddingBottom), heightVal);
        }
    }
}
