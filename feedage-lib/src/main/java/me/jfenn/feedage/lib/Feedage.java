package me.jfenn.feedage.lib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.jfenn.feedage.lib.data.CategoryData;
import me.jfenn.feedage.lib.data.FeedData;
import me.jfenn.feedage.lib.utils.CacheInterface;

public class Feedage implements FeedData.OnFeedLoadedListener {

    private FeedData[] feeds;
    private ExecutorService service;
    private OnCategoriesUpdatedListener listener;
    private boolean hasOrganized;

    public Feedage(CacheInterface cache, FeedData... feeds) {
        this.feeds = feeds;
        service = Executors.newSingleThreadExecutor();

        for (FeedData feed : feeds)
            feed.loadCache(cache);
    }

    public void getNext(OnCategoriesUpdatedListener listener) {
        this.listener = listener;
        for (FeedData feed : feeds) {
            if (feed.getPage() == 0 || feed.isPaginated())
                feed.getNext(service, this);
        }
    }

    @Override
    public void onFeedLoaded(FeedData feed, boolean shouldReorganize) {
        List<FeedData> feeds = Arrays.asList(this.feeds);
        if (listener != null) {
            if (shouldReorganize || !hasOrganized) {
                listener.onCategoriesUpdated(CategoryData.getCategories(feeds));
                hasOrganized = true;
            }
            listener.onFeedsUpdated(new ArrayList<>(feeds));
        }
    }

    public interface OnCategoriesUpdatedListener {
        void onFeedsUpdated(List<FeedData> feeds);
        void onCategoriesUpdated(List<CategoryData> categories);
    }
}
