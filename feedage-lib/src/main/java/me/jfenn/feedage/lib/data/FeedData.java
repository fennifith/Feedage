package me.jfenn.feedage.lib.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public abstract class FeedData {

    private String url;
    private String name;
    private String homepage;

    private List<PostData> posts;
    private int pages;
    private int pageStart;

    private OnFeedLoadedListener listener;

    public FeedData(String url) {
        this(url, 0);
    }

    public FeedData(String url, int pageStart) {
        this.url = url;
        this.homepage = url;
        this.pageStart = pageStart;
        posts = new ArrayList<>();
    }

    void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getHomepage() {
        return homepage;
    }

    public String getBasicHomepage() {
        String basicHomepage = url.substring(url.contains("//") ? url.indexOf("//") + 2 : 0);
        return (basicHomepage.contains("/") ? basicHomepage.substring(0, basicHomepage.indexOf("/")) : basicHomepage)
                .replace("www.", "");
    }

    public final List<PostData> getPosts() {
        return posts;
    }

    public final int getPage() {
        return pages;
    }

    public final boolean isPaginated() {
        return url.contains("%s");
    }

    public final void getNext(ExecutorService service, OnFeedLoadedListener listener) {
        this.listener = listener;

        service.execute(() -> {
            HttpURLConnection connection = null;
            InputStream stream = null;
            BufferedReader reader = null;
            StringBuilder response = new StringBuilder();
            try {
                connection = (HttpURLConnection) new URL(url.contains("%s") ? String.format(url, pages + pageStart) : url).openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                    response.append('\n');
                }
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();

            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ignored) {
                }
            }

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }

            pages++;
            onFeedLoaded(response.toString());
        });
    }

    abstract List<PostData> parseContent(String content);

    private void onFeedLoaded(String content) {
        posts.addAll(parseContent(content));

        if (listener != null)
            listener.onFeedLoaded(this);
    }

    public interface OnFeedLoadedListener {
        void onFeedLoaded(FeedData feed);
    }
}
