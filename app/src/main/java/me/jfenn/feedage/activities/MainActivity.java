package me.jfenn.feedage.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import me.jfenn.feedage.R;
import me.jfenn.feedage.adapters.ItemAdapter;
import me.jfenn.feedage.data.items.CategoryItemData;
import me.jfenn.feedage.data.items.ItemData;
import me.jfenn.feedage.lib.FeedageLib;
import me.jfenn.feedage.lib.data.AtomFeedData;
import me.jfenn.feedage.lib.data.CategoryData;
import me.jfenn.feedage.lib.data.FeedData;
import me.jfenn.feedage.utils.HackyCacheInterface;
import me.jfenn.feedage.views.ProgressLineView;

public class MainActivity extends AppCompatActivity implements FeedageLib.OnCategoriesUpdatedListener {

    private RecyclerView recycler;
    private ProgressLineView progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler = findViewById(R.id.recycler);
        progress = findViewById(R.id.progress);

        recycler.setLayoutManager(new LinearLayoutManager(this));

        FeedageLib feedage = new FeedageLib(
                new HackyCacheInterface(PreferenceManager.getDefaultSharedPreferences(this)),
                new AtomFeedData("https://www.androidpolice.com/feed/?paged=%s", 1, Color.parseColor("#af1c1c"), Color.WHITE),
                new AtomFeedData("https://www.androidauthority.com/feed/?paged=%s", 1, Color.parseColor("#01e0bd"), Color.BLACK),
                new AtomFeedData("https://www.theverge.com/rss/index.xml", Color.parseColor("#e5127d"), Color.WHITE),
                new AtomFeedData("https://techaeris.com/feed/?paged=%s", 1, Color.parseColor("#212121"), Color.WHITE),
                new AtomFeedData("https://www.engadget.com/rss.xml", Color.WHITE, Color.BLACK),
                new AtomFeedData("http://rss.nytimes.com/services/xml/rss/nyt/Technology.xml", Color.WHITE, Color.BLACK),
                new AtomFeedData("https://www.xda-developers.com/feed/?paged=%s", 1, Color.parseColor("#f59714"), Color.BLACK),
                new AtomFeedData("https://www.wired.com/feed", Color.parseColor("#BDBDBD"), Color.BLACK)
        );

        feedage.getNext(this);
    }

    @Override
    public void onFeedsUpdated(final List<FeedData> feeds) {
        new Handler(Looper.getMainLooper()).post(() -> {
            int loaded = 0;
            for (FeedData feed : feeds) {
                if (!feed.isLoading())
                    loaded++;
            }

            progress.update((float) loaded / feeds.size());
            progress.setVisibility(loaded == feeds.size() ? View.GONE : View.VISIBLE);
        });
    }

    @Override
    public void onCategoriesUpdated(final List<CategoryData> categories) {
        Log.d("CategoriesLoaded", categories.size() + "");

        new Handler(Looper.getMainLooper()).post(() -> {
            List<ItemData> items = new ArrayList<>();
            for (CategoryData category : categories)
                items.add(new CategoryItemData(category));

            if (recycler.getAdapter() == null)
                recycler.setAdapter(new ItemAdapter(items));
            else recycler.swapAdapter(new ItemAdapter(items), false);
        });
    }
}
