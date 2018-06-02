package me.jfenn.feedage.views;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;

public class VerticalTextView extends AppCompatTextView {

    public VerticalTextView(Context context) {
        this(context, null);
    }

    public VerticalTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setMaxLines(getHeight() / getLineHeight());
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }
}
