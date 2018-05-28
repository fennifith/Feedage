package me.jfenn.feedage.lib.data;

import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.jfenn.feedage.lib.utils.SOAMCOS;

public class PostData {

    private String title;
    private String description;
    private String content;
    private String imageUrl;
    private transient List<String> tags;

    private transient Date publishDate;
    private transient Date updateDate;
    private transient List<AuthorData> authors;

    private transient FeedData parent;
    private transient SOAMCOS chain;

    public PostData(FeedData parent) {
        this.parent = parent;
        tags = new ArrayList<>();
        authors = new ArrayList<>();
    }

    public PostData(FeedData parent, PostData post) {
        this.parent = parent;
        tags = new ArrayList<>();
        authors = new ArrayList<>();
        this.title = post.title;
        this.description = post.description;
        this.content = post.content;
        this.imageUrl = post.imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title != null)
            this.title = Jsoup.parse(title).text();
    }

    public String getDescription() {
        return description;
    }

    public String getDescriptionText() {
        if (parent.getBasicHomepage().equals("theverge.com"))
            System.out.println("Description: " + description + ", Content: " + getContentText());
        return description != null && description.length() > 0 ? description : getContentText();
    }

    public void setDescription(String description) {
        if (description != null)
            this.description = Jsoup.parse(description).text();
    }

    public String getContent() {
        return content;
    }

    public String getContentText() {
        return content != null ? Jsoup.parse(content).text() : null;
    }

    public String getHTML() {
        if (content != null) {
            Document document = Jsoup.parse(content);
            document.select("img").remove();
            return document.toString();
        } else return getDescription();
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        if (imageUrl != null)
            return imageUrl;
        else if (content != null) {
            Element element = Jsoup.parse(content).selectFirst("img");
            return element != null && element.hasAttr("src") ? element.attr("src") : null;
        } else return null;
    }

    public void setPublishDate(String publishDate) {
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setUpdateDate(String updateDate) {
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void addTag(String tag) {
        tags.add(tag);
    }

    public List<String> getTags() {
        return tags;
    }

    public void addAuthor(AuthorData author) {
        authors.add(author);
    }

    public List<AuthorData> getAuthors() {
        return authors;
    }

    public FeedData getParent() {
        return parent;
    }

    public SOAMCOS getChain() {
        if (chain == null)
            chain = new SOAMCOS(this);

        return chain;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PostData) {
            PostData post = (PostData) o;
            return (title != null && title.equals(post.title))
                    || (content != null && content.equals(post.content));
        } else return super.equals(o);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static PostData fromString(FeedData parent, String json) {
        if (json != null)
            return new PostData(parent, new Gson().fromJson(json, PostData.class));
        else return null;
    }
}
