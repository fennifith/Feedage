package me.jfenn.feedage.activities;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import me.jfenn.feedage.Feedage;
import me.jfenn.feedage.R;
import me.jfenn.feedage.adapters.ItemAdapter;
import me.jfenn.feedage.data.items.FeedPreferenceItemData;
import me.jfenn.feedage.data.items.ItemData;
import me.jfenn.feedage.lib.data.FeedData;

public class SettingsActivity extends AppCompatActivity {

    private Feedage feedage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        feedage = (Feedage) getApplicationContext();

        Toolbar toolbar = findViewById(R.id.toolbar);
        RecyclerView feeds = findViewById(R.id.feeds);

        List<ItemData> items = new ArrayList<>();
        for (FeedData feed : feedage.getFeeds())
            items.add(new FeedPreferenceItemData(feed));

        feeds.setLayoutManager(new LinearLayoutManager(this));
        feeds.setAdapter(new ItemAdapter(items));

        findViewById(R.id.newFeed).setOnClickListener(v -> {

        });

        Drawable icon = VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back, getTheme());
        if (icon != null) {
            DrawableCompat.setTint(icon, Color.BLACK);
            toolbar.setNavigationIcon(icon);
            setSupportActionBar(toolbar);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }
}
