package me.jfenn.feedage.lib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.jfenn.feedage.lib.data.CategoryData;
import me.jfenn.feedage.lib.data.FeedData;
import me.jfenn.feedage.lib.utils.CacheInterface;

public class FeedageLib implements FeedData.OnFeedLoadedListener {

    private FeedData[] feeds;
    private ExecutorService service;
    private OnCategoriesUpdatedListener listener;
    private boolean hasOrganized;

    /**
     * Create a FeedageLib object from a set of feeds
     *
     * @param cache interface used to store data temporarily
     * @param feeds sources to fetch data from
     */
    public FeedageLib(CacheInterface cache, FeedData... feeds) {
        this.feeds = feeds;
        service = Executors.newSingleThreadExecutor();

        for (FeedData feed : feeds)
            feed.loadCache(cache);
    }

    /**
     * Load the next data from the given sources
     *
     * @param listener listener for when the loading is complete
     */
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
            listener.onFeedsUpdated(new ArrayList<>(feeds));
            if (shouldReorganize || !hasOrganized) {
                listener.onCategoriesUpdated(CategoryData.getCategories(feeds));
                hasOrganized = true;
            }
        }
    }

    public FeedData[] getFeeds() {
        return feeds;
    }

    public interface OnCategoriesUpdatedListener {
        void onFeedsUpdated(List<FeedData> feeds);
        void onCategoriesUpdated(List<CategoryData> categories);
    }
}
