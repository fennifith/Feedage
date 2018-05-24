package me.jfenn.feedage.lib.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.jfenn.feedage.lib.utils.SortOfAMarkovChainOrSomething;

public class PostData {

    private String title;
    private String description;
    private String content;
    private String imageUrl;
    private List<String> tags;

    private Date publishDate;
    private Date updateDate;
    private List<AuthorData> authors;

    private FeedData parent;
    private SortOfAMarkovChainOrSomething chain;

    public PostData(FeedData parent) {
        this.parent = parent;
        tags = new ArrayList<>();
        authors = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl(String imageUrl) {
        if (imageUrl != null)
            return imageUrl;
        else if (content != null) {
            Matcher matcher = Pattern.compile("(<img)([\"A-Za-z0-9 =-_]*)(src=\")([A-Za-z0-9./?-_:]*)(\")").matcher(content);
            if (matcher.find())
                return matcher.group(4);
        }

        return null;
    }

    void setPublishDate(String publishDate) {
    }

    public Date getPublishDate() {
        return publishDate;
    }

    void setUpdateDate(String updateDate) {
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    void addTag(String tag) {
        tags.add(tag);
    }

    public List<String> getTags() {
        return tags;
    }

    void addAuthor(AuthorData author) {
        authors.add(author);
    }

    public List<AuthorData> getAuthors() {
        return authors;
    }

    public FeedData getParent() {
        return parent;
    }

    public SortOfAMarkovChainOrSomething getChain() {
        if (chain == null)
            chain = new SortOfAMarkovChainOrSomething(this);

        return chain;
    }
}
