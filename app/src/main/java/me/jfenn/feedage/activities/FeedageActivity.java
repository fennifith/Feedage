package me.jfenn.feedage.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

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
}
