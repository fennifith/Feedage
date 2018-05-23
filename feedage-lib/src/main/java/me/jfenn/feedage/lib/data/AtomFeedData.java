package me.jfenn.feedage.lib.data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

public class AtomFeedData extends FeedData {

    public AtomFeedData(String url) {
        super(url);
    }

    public AtomFeedData(String url, int pageStart) {
        super(url, pageStart);
    }

    @Override
    List<PostData> parseContent(String content) {
        List<PostData> posts = new ArrayList<>();
        Document document = Jsoup.parse(content);
        Element root = document;
        while (document.children().size() == 1)
            root = root.children().get(0);

        return posts;
    }
}
