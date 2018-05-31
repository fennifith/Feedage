package me.jfenn.feedage;

import android.app.Application;
import android.content.SharedPreferences;
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
import me.jfenn.feedage.lib.data.PostData;
import me.jfenn.feedage.utils.HackyCacheInterface;
import me.jfenn.feedage.utils.PreferenceUtils;

public class Feedage extends Application implements FeedageLib.OnCategoriesUpdatedListener {

    private FeedageLib feedage;
    private List<FeedageLib.OnCategoriesUpdatedListener> listeners;
    private OnProgressUpdateListener progressListener;

    private List<FeedData> feeds;
    private List<CategoryData> categories;
    private List<PostData> bookmarks;

    private SharedPreferences prefs;
    private boolean isLoading;

    @Override
    public void onCreate() {
        super.onCreate();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        listeners = new ArrayList<>();
        feeds = new ArrayList<>();
        categories = PreferenceUtils.getCategoryList(prefs, "categories");
        bookmarks = PreferenceUtils.getPostList(prefs, "bookmarks");

        List<String> feedUrls = PreferenceUtils.getStringList(prefs, "feeds");
        if (feedUrls.size() == 0) {
            feedUrls.addAll(Arrays.asList(
                    "https://www.androidpolice.com/feed/",
                    "https://www.androidauthority.com/feed/",
                    "https://www.theverge.com/rss/index.xml",
                    "https://techaeris.com/feed/",
                    "https://www.engadget.com/rss.xml",
                    "http://rss.nytimes.com/services/xml/rss/nyt/Technology.xml",
                    "https://www.xda-developers.com/feed/",
                    "https://www.wired.com/feed"
            ));
        }

        for (String url : feedUrls) {
            int textColor = Color.BLACK, backgroundColor = Color.WHITE;
            if (url.contains("androidpolice")) {
                textColor = Color.WHITE;
                backgroundColor = Color.parseColor("#af1c1c");
            } else if (url.contains("androidauthority")) {
                backgroundColor = Color.parseColor("#01e0bd");
            } else if (url.contains("theverge")) {
                textColor = Color.WHITE;
                backgroundColor = Color.parseColor("#e5127d");
            } else if (url.contains("techaeris")) {
                textColor = Color.WHITE;
                backgroundColor = Color.parseColor("#212121");
            } else if (url.contains("xda-developers")) {
                backgroundColor = Color.parseColor("#f59714");
            } else if (url.contains("wired")) {
                backgroundColor = Color.parseColor("#BDBDBD");
            }

            feeds.add(new AtomFeedData(url, backgroundColor, textColor));
        }

        feedage = new FeedageLib(
                new HackyCacheInterface(prefs),
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

    public List<PostData> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarked(PostData post, boolean isBookmarked) {
        if (isBookmarked)
            bookmarks.add(post);
        else bookmarks.remove(post);

        PreferenceUtils.putPostList(prefs.edit(), "bookmarks", bookmarks).apply();
    }

    public boolean isBookmarked(PostData post) {
        return bookmarks.contains(post);
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
            PreferenceUtils.putCategoryList(prefs.edit(), "categories", categories).apply();
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
