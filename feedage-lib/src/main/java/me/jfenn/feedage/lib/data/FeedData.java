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

import me.jfenn.feedage.lib.utils.CacheInterface;

public abstract class FeedData {

    private String url, name, homepage;
    private int backgroundColor, textColor;

    private List<PostData> posts;
    private int pages, pageStart;
    private boolean isLoading;

    private OnFeedLoadedListener listener;
    private CacheInterface cache;

    public FeedData(String url, int backgroundColor, int textColor) {
        this(url, 0, backgroundColor, textColor);
    }

    public FeedData(String url, int pageStart, int backgroundColor, int textColor) {
        this.url = homepage = url;
        this.pageStart = pageStart;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        posts = new ArrayList<>();
    }

    public void loadCache(CacheInterface cache) {
        this.cache = cache;

        if (cache != null) {
            int number;
            try {
                number = Integer.parseInt(cache.getCache(url + "-length"));
            } catch (Exception e) {
                return;
            }

            for (int i = 0; i < number; i++) {
                PostData post = PostData.fromString(this, cache.getCache(url + "-post" + i));
                if (post != null)
                    posts.add(post);
            }
        }
    }

    public String getUrl() {
        return url;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getTextColor() {
        return textColor;
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
                .replace("www.", "")
                .replace("rss.", "");
    }

    public boolean isLoading() {
        return isLoading;
    }

    public final List<PostData> getPosts() {
        return new ArrayList<>(posts);
    }

    public final int getPage() {
        return pages;
    }

    public final boolean isPaginated() {
        return url.contains("%s");
    }

    public final void getNext(ExecutorService service, OnFeedLoadedListener listener) {
        this.listener = listener;
        isLoading = true;

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
        List<PostData> newPosts = parseContent(content);
        int newCount = 0;
        for (PostData newPost : newPosts) {
            if (!posts.contains(newPost)) {
                posts.add(newPost);
                newCount++;
            }
        }

        if (newCount > 0) {
            if (cache != null) {
                cache.putCache(url + "-length", posts.size() + "");
                for (int i = 0; i < posts.size(); i++)
                    cache.putCache(url + "-post" + i, posts.get(i).toString());
            }
        }

        isLoading = false;
        if (listener != null)
            listener.onFeedLoaded(this, newCount > 0);
    }

    public interface OnFeedLoadedListener {
        void onFeedLoaded(FeedData feed, boolean reorganize);
    }
}
