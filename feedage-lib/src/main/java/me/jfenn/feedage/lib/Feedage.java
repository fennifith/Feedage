package me.jfenn.feedage.lib;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.jfenn.feedage.lib.data.FeedData;

public class Feedage implements FeedData.OnFeedLoadedListener {

    private FeedData[] feeds;
    private ExecutorService service;
    private OnPostsLoadedListener listener;

    public Feedage(FeedData... feeds) {
        this.feeds = feeds;
        service = Executors.newSingleThreadExecutor();
    }

    public void setListener(OnPostsLoadedListener listener) {
        this.listener = listener;
    }

    public void getNext() {
        for (FeedData feed : feeds) {
            if (feed.getPage() == 0 || feed.isPaginated())
                feed.getNext(service, this);
        }
    }

    @Override
    public void onFeedLoaded(FeedData feed) {
        if (listener != null)
            listener.onPostsLoaded(feed);
    }

    public interface OnPostsLoadedListener {
        void onPostsLoaded(FeedData feed);
    }
}
