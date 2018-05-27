package me.jfenn.feedage.lib.data;

public class AuthorData {

    private String name;
    private String homepage;
    private String imageUrl;

    public AuthorData(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
