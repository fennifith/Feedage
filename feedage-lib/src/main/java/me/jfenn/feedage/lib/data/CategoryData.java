package me.jfenn.feedage.lib.data;

import java.util.ArrayList;
import java.util.List;

public class CategoryData {

    private String title;
    private List<PostData> posts;

    public CategoryData(String title, List<PostData> posts) {
        this.title = title;
        this.posts = posts;
    }

    public static List<CategoryData> getCategories(List<FeedData> feeds) {
        List<CategoryData> categories = new ArrayList<>();
        return categories;
    }

}
