package me.jfenn.feedage.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;

import me.jfenn.feedage.utils.ColorUtils;
import me.jfenn.feedage.utils.DimenUtils;

public class ColorView extends View {

    @ColorInt
    int color = Color.BLACK;
    Paint paint, outlinePaint;

    public ColorView(final Context context) {
        this(context, null);
    }

    public ColorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);

        outlinePaint = new Paint();
        outlinePaint.setAntiAlias(true);
        outlinePaint.setStyle(Paint.Style.STROKE);
        outlinePaint.setStrokeWidth(DimenUtils.getPixelsFromDp(2));
        outlinePaint.setColor(Color.BLACK);
    }

    public void setColor(@ColorInt int color) {
        this.color = color;
        paint.setColor(color);
        outlinePaint.setColor(ColorUtils.isColorDark(color) ? Color.TRANSPARENT : Color.BLACK);
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        int size = Math.min(getWidth(), getHeight());
        int outline = (int) DimenUtils.getPixelsFromDp(2);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, (size / 2) - outline, paint);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, (size / 2) - outline, outlinePaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int size = getMeasuredWidth();
        setMeasuredDimension(size, size);
    }

}
