package me.jfenn.feedage.activities;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import me.jfenn.feedage.Feedage;
import me.jfenn.feedage.R;
import me.jfenn.feedage.adapters.ItemAdapter;
import me.jfenn.feedage.data.items.FeedPreferenceItemData;
import me.jfenn.feedage.data.items.ItemData;
import me.jfenn.feedage.lib.data.FeedData;

public class SettingsActivity extends FeedageActivity {

    private SharedPreferences prefs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        RecyclerView feeds = findViewById(R.id.feeds);
        AppCompatSpinner theme = findViewById(R.id.theme);

        List<ItemData> items = new ArrayList<>();
        for (FeedData feed : getFeedage().getFeeds())
            items.add(new FeedPreferenceItemData(feed));

        feeds.setLayoutManager(new LinearLayoutManager(this));
        feeds.setAdapter(new ItemAdapter(items));

        findViewById(R.id.newFeed).setOnClickListener(v -> {

        });

        theme.setAdapter(ArrayAdapter.createFromResource(this, R.array.themes, R.layout.support_simple_spinner_dropdown_item));
        theme.setSelection(prefs.getInt(Feedage.PREF_THEME, Feedage.THEME_LIGHT));
        theme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                prefs.edit().putInt(Feedage.PREF_THEME, position).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
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
