package me.jfenn.feedage.lib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.jfenn.feedage.lib.data.CategoryData;
import me.jfenn.feedage.lib.data.FeedData;
import me.jfenn.feedage.lib.utils.CacheInterface;
import me.jfenn.feedage.lib.utils.threading.CancelableRunnable;
import me.jfenn.feedage.lib.utils.threading.ExecutorServiceWrapper;

public class FeedageLib implements FeedData.OnFeedLoadedListener {

    private FeedData[] feeds;
    private ExecutorServiceWrapper service;
    private OnCategoriesUpdatedListener listener;
    private boolean hasOrganized;

    private List<CancelableRunnable> runnables;

    /**
     * Create a FeedageLib object from a set of feeds
     *
     * @param cache interface used to store data temporarily
     * @param feeds sources to fetch data from
     */
    public FeedageLib(CacheInterface cache, FeedData... feeds) {
        this.feeds = feeds;
        runnables = new ArrayList<>();
        service = new ExecutorServiceWrapper();

        for (FeedData feed : feeds)
            feed.loadCache(cache);
    }

    /**
     * Load the next data from the given sources
     *
     * @param listener listener for when the loading is complete
     */
    public void getNext(OnCategoriesUpdatedListener listener) {
        service.cancel();

        this.listener = listener;
        for (FeedData feed : feeds) {
            if (feed.getPage() == 0 || feed.isPaginated())
                feed.getNext(service, this);
        }

        service.end();
    }

    public void stopLoading() {
        service.end();
        service.cancel();
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
