package com.daasuu.easinginterpolator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.FrameLayout;

import com.daasuu.ei.Ease;
import com.daasuu.ei.EasingInterpolator;

/**
 * Created by sudamasayuki on 16/03/19.
 */
public class EasingGraphView extends FrameLayout {

    private Paint mPaint = new Paint();
    private Paint mFramePaint = new Paint();
    private Paint mTextPaint = new Paint();

    private EasingInterpolator easingInterpolator;
    private float mAdjustTextMesureY = -1;

    private float dpSize;

    private final int loopSize = 100;

    public EasingGraphView(Context context) {
        this(context, null, 0);
    }

    public EasingGraphView(Context context, Ease ease) {
        this(context, null, 0);
        easingInterpolator = new EasingInterpolator(ease);
    }

    public EasingGraphView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EasingGraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint.setColor(Color.BLUE);
        mPaint.setAntiAlias(true);

        mFramePaint.setColor(Color.BLUE);
        mFramePaint.setAntiAlias(true);
        mFramePaint.setStyle(Paint.Style.STROKE);

        dpSize = convertDpToPixel(1, context);

        mTextPaint.setTextSize(convertDpToPixel(12, context));
        mAdjustTextMesureY = mTextPaint.getTextSize();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(110 * (int) dpSize, 180 * (int) dpSize);
    }

    private static float convertDpToPixel(float dp, Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (easingInterpolator == null) {
            easingInterpolator = new EasingInterpolator(Ease.ELASTIC_IN_OUT);
        }

        int xWidth = (int) mTextPaint.measureText(easingInterpolator.getEase().name());
        float x = (loopSize * dpSize - xWidth) / 2f;
        if (x < 0) {
            x = 0;
        }
        canvas.drawText(easingInterpolator.getEase().name(), x, mAdjustTextMesureY, mTextPaint);


        PointF points[] = new PointF[loopSize];
        for (int i = 0; i < loopSize; i++) {
            points[i] = new PointF();
            points[i].x = i * dpSize;
            points[i].y = (loopSize - easingInterpolator.getInterpolation((float) i / loopSize) * loopSize) * dpSize;
        }

        canvas.save();
        {
            canvas.translate(0, 40 * dpSize);
            for (int i = 0, n = loopSize - 1; i < n; i++) {
                canvas.drawLine(points[i].x, points[i].y,
                        points[i + 1].x, points[i + 1].y, mPaint);
            }
            canvas.drawRect(0, 0, loopSize * dpSize, loopSize * dpSize, mFramePaint);
        }
        canvas.restore();

    }


}
