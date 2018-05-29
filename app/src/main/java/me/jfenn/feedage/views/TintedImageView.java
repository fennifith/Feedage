package me.jfenn.feedage.views;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class TintedImageView extends AppCompatImageView {

    private int color;

    public TintedImageView(Context context) {
        this(context, null);
    }

    public TintedImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TintedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void tint(@ColorInt int color) {
        ValueAnimator animator = ValueAnimator.ofObject(new ArgbEvaluator(), this.color, color);
        animator.addUpdateListener(animation -> {
            TintedImageView.this.color = (int) animation.getAnimatedValue();
            setColorFilter(TintedImageView.this.color);
        });
        animator.start();
    }

    public void setTint(@ColorInt int color) {
        this.color = color;
        setColorFilter(color);
    }

}
