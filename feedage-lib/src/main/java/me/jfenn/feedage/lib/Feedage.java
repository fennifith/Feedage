package me.jfenn.feedage.lib;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.jfenn.feedage.lib.data.AtomFeedData;
import me.jfenn.feedage.lib.data.FeedData;
import me.jfenn.feedage.lib.data.PostData;

public class Feedage implements FeedData.OnFeedLoadedListener {

    private FeedData[] feeds;
    private ExecutorService service;

    public Feedage(FeedData... feeds) {
        this.feeds = feeds;
        service = Executors.newSingleThreadExecutor();
    }

    public void getNext() {
        for (FeedData feed : feeds) {
            if (feed.getPage() == 0 || feed.isPaginated())
                feed.getNext(service, this);
        }
    }

    @Override
    public void onFeedLoaded(FeedData feed) {
        for (PostData post : feed.getPosts()) {
            System.out.println(post);
        }
    }

    public static void main(String... args) {
        Feedage feedage = new Feedage(
                new AtomFeedData("https://www.androidpolice.com/feed/?paged=%s", 1),
                new AtomFeedData("https://www.androidauthority.com/feed/?paged=%s", 1),
                new AtomFeedData("https://www.theverge.com/rss/index.xml")
        );

        feedage.getNext();
    }
}
