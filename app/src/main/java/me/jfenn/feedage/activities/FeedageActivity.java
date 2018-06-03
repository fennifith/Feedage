package me.jfenn.feedage.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import me.jfenn.feedage.Feedage;

public class FeedageActivity extends AppCompatActivity {

    private Feedage feedage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        feedage = (Feedage) getApplicationContext();
        setTheme(feedage.getThemeRes());
    }

    public Feedage getFeedage() {
        return feedage;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        for (int i = 0; i < menu.size(); i++) {
            Drawable icon = menu.getItem(i).getIcon();
            if (icon != null)
                DrawableCompat.setTint(icon, feedage.getTextColorSecondary());
        }

        return super.onPrepareOptionsMenu(menu);
    }
}
