package me.jfenn.feedage;

import android.app.Application;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.jfenn.feedage.lib.FeedageLib;
import me.jfenn.feedage.lib.data.AtomFeedData;
import me.jfenn.feedage.lib.data.CategoryData;
import me.jfenn.feedage.lib.data.FeedData;
import me.jfenn.feedage.utils.HackyCacheInterface;

public class Feedage extends Application implements FeedageLib.OnCategoriesUpdatedListener {

    private FeedageLib feedage;
    private List<FeedageLib.OnCategoriesUpdatedListener> listeners;
    private OnProgressUpdateListener progressListener;

    private List<FeedData> feeds;
    private List<CategoryData> categories;

    private boolean isLoading;

    @Override
    public void onCreate() {
        super.onCreate();
        listeners = new ArrayList<>();
        feeds = new ArrayList<>();
        categories = new ArrayList<>();

        feeds.addAll(Arrays.asList(
                new AtomFeedData("https://www.androidpolice.com/feed/?paged=%s", 1, Color.parseColor("#af1c1c"), Color.WHITE),
                new AtomFeedData("https://www.androidauthority.com/feed/?paged=%s", 1, Color.parseColor("#01e0bd"), Color.BLACK),
                new AtomFeedData("https://www.theverge.com/rss/index.xml", Color.parseColor("#e5127d"), Color.WHITE),
                new AtomFeedData("https://techaeris.com/feed/?paged=%s", 1, Color.parseColor("#212121"), Color.WHITE),
                new AtomFeedData("https://www.engadget.com/rss.xml", Color.WHITE, Color.BLACK),
                new AtomFeedData("http://rss.nytimes.com/services/xml/rss/nyt/Technology.xml", Color.WHITE, Color.BLACK),
                new AtomFeedData("https://www.xda-developers.com/feed/?paged=%s", 1, Color.parseColor("#f59714"), Color.BLACK),
                new AtomFeedData("https://www.wired.com/feed", Color.parseColor("#BDBDBD"), Color.BLACK)
        ));

        feedage = new FeedageLib(
                new HackyCacheInterface(PreferenceManager.getDefaultSharedPreferences(this)),
                feeds.toArray(new FeedData[feeds.size()])
        );

        getNext();
    }

    public void addListener(FeedageLib.OnCategoriesUpdatedListener listener) {
        listeners.add(listener);
    }

    public void removeListener(FeedageLib.OnCategoriesUpdatedListener listener) {
        listeners.remove(listener);
    }

    public void setProgressListener(OnProgressUpdateListener listener) {
        progressListener = listener;
    }

    public void getNext() {
        feedage.getNext(this);
        isLoading = true;
    }

    public List<FeedData> getFeeds() {
        return feeds;
    }

    public List<CategoryData> getCategories() {
        return categories;
    }

    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public void onFeedsUpdated(final List<FeedData> feeds) {
        this.feeds = feeds;
        new Handler(Looper.getMainLooper()).post(() -> {
            for (FeedageLib.OnCategoriesUpdatedListener listener : listeners)
                listener.onFeedsUpdated(feeds);

            onProgressUpdate(feeds);
        });
    }

    @Override
    public void onCategoriesUpdated(final List<CategoryData> categories) {
        this.categories = categories;
        new Handler(Looper.getMainLooper()).post(() -> {
            for (FeedageLib.OnCategoriesUpdatedListener listener : listeners)
                listener.onCategoriesUpdated(categories);

            onProgressUpdate(feeds);
        });
    }

    private void onProgressUpdate(List<FeedData> feeds) {
        int loaded = 0;
        for (FeedData feed : feeds) {
            if (!feed.isLoading())
                loaded++;
        }

        isLoading = loaded == feeds.size();
        if (progressListener != null)
            progressListener.onProgressUpdate(isLoading, (float) loaded / feeds.size());
    }

    public interface OnProgressUpdateListener {
        void onProgressUpdate(boolean isLoading, float progress);
    }
}
