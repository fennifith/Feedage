package me.jfenn.feedage.utils.links;

import android.content.Context;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.text.Layout;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Patterns;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;

public class CustomTabsMovementMethod extends LinkMovementMethod {

    private Context context;

    private final GestureDetector gestureDetector;
    private TextView textView;
    private Spannable buffer;

    public CustomTabsMovementMethod(Context context) {
        this.context = context;
        gestureDetector = new GestureDetector(context, new SimpleOnGestureListener());
    }

    @Override
    public boolean onTouchEvent(TextView textView, Spannable buffer, MotionEvent event) {
        this.textView = textView;
        this.buffer = buffer;
        gestureDetector.onTouchEvent(event);
        return false;
    }

    private class SimpleOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= textView.getTotalPaddingLeft();
            y -= textView.getTotalPaddingTop();
            x += textView.getScrollX();
            y += textView.getScrollY();

            Layout layout = textView.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            URLSpan[] link = buffer.getSpans(off, off, URLSpan.class);
            if (link.length > 0) {
                String linkText = link[0].getURL();
                if (Patterns.WEB_URL.matcher(linkText).matches())
                    new CustomTabsIntent.Builder()
                            .build()
                            .launchUrl(context, Uri.parse(linkText));
                return true;
            }

            return false;
        }
    }

}
