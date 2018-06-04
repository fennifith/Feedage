package me.jfenn.feedage.lib.data;

import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.jfenn.feedage.lib.utils.SOAMCOS;

public class PostData {

    private static final String DEFAULT_DATE_FORMAT = "EEEE, MMMM d, yyyy";

    private String title;
    private String description;
    private String content;
    private String imageUrl;
    private String sourceUrl;
    private List<String> tags;

    private transient Date publishDate;
    private transient Date updateDate;
    private String publishDateString;
    private String updateDateString;
    private List<AuthorData> authors;

    private transient FeedData parent;
    private transient SOAMCOS chain;

    public PostData(FeedData parent) {
        this.parent = parent;
        tags = new ArrayList<>();
        authors = new ArrayList<>();
    }

    public PostData(FeedData parent, PostData post) {
        this.parent = parent;
        this.title = post.title;
        this.description = post.description;
        this.content = post.content;
        this.imageUrl = post.imageUrl;
        this.sourceUrl = post.sourceUrl;
        tags = post.tags != null ? new ArrayList<>(post.tags) : new ArrayList<>();
        authors = post.authors != null ? new ArrayList<>(post.authors) : new ArrayList<>();

        setPublishDate(post.publishDateString);
        setUpdateDate(post.updateDateString);
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

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getSourceUrl() {
        return sourceUrl != null ? sourceUrl : "https://" + parent.getBasicHomepage();
    }

    public void setPublishDate(String publishDate) {
        if (publishDate != null) {
            publishDateString = publishDate;
            this.publishDate = parseDateString(publishDate);
        }
    }

    public String getPublishDateString() {
        if (publishDate != null)
            return formatDate(publishDate);
        else return publishDateString;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setUpdateDate(String updateDate) {
        if (updateDate != null) {
            updateDateString = updateDate;
            this.updateDate = parseDateString(updateDate);
        }
    }

    public String getUpdateDateString() {
        if (updateDate != null)
            return formatDate(updateDate);
        else return updateDateString;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    private Date parseDateString(String dateString) {
        return parseDateString(dateString, new ArrayList<>(Arrays.asList(
                DEFAULT_DATE_FORMAT,
                "EEE, dd MMM yyyy HH:mm:ss Z",
                "EEE, d MMM yyyy HH:mm:ss z",
                "EEE MMM d HH:mm z yyyy",
                "dd MMM yyyy HH:mm:ss z",
                "yyyy-MM-dd'T'HH:mm:ssZ"
        )));
    }

    private Date parseDateString(String dateString, List<String> formats) {
        if (formats.size() > 0) {
            DateFormat format = new SimpleDateFormat(formats.get(0), Locale.getDefault());
            try {
                return format.parse(dateString);
            } catch (ParseException e) {
                formats.remove(0);
                return parseDateString(dateString, formats);
            }
        } else return null;
    }

    private String formatDate(Date date) {
        if (date != null)
            return new SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault()).format(date);
        else return null;
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

    public void addAuthor(String authors) {
        if (authors == null || authors.length() < 1)
            return;

        if (authors.contains(", ")) {
            for (String author : authors.split(", "))
                addAuthor(author);

            return;
        }

        if (authors.contains(" and ")) {
            for (String author : authors.split(" and "))
                addAuthor(author);

            return;
        }

        this.authors.add(new AuthorData(authors));
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
