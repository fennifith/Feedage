package me.jfenn.feedage.lib.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.jfenn.feedage.lib.utils.SortOfAMarkovChainOrSomething;

public class CategoryData implements Comparable<CategoryData> {

    private List<PostData> posts;
    private List<SortOfAMarkovChainOrSomething.WordAverage> averages;

    public CategoryData() {
        posts = new ArrayList<>();
        averages = new ArrayList<>();
    }

    public String getTitle() {
        SortOfAMarkovChainOrSomething.WordAverage average = averages.get(0);
        if (average != null) {
            return String.valueOf(average.getWord1().charAt(0)).toUpperCase()
                    + average.getWord1().substring(1) + " "
                    + String.valueOf(average.getWord2().charAt(0)).toUpperCase()
                    + average.getWord2().substring(1);
        } else return null;
    }

    void setPosts(List<PostData> posts) {
        this.posts = posts;
    }

    public List<PostData> getPosts() {
        return posts;
    }

    void setAverages(List<SortOfAMarkovChainOrSomething.WordAverage> averages) {
        this.averages = averages;
    }

    public String getDescription() {
        return getDescription(10);
    }

    public String getDescription(int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 1; (length < 1 || i < length) && i < averages.size(); i++)
            builder.append(averages.get(i).toString());

        return builder.toString();
    }

    public static List<CategoryData> getCategories(List<FeedData> feeds) {
        List<CategoryData> categories = new ArrayList<>();
        List<PostData> allPosts = new ArrayList<>();
        for (FeedData feed : feeds)
            allPosts.addAll(feed.getPosts());

        double maxThreshold = 0;
        for (int i = 0; i < allPosts.size(); i++)
            maxThreshold = ((maxThreshold * i) + getThreshold(allPosts.get(i).getChain(), allPosts)) / (i + 1);

        for (int i = 0; i < allPosts.size(); i++) {
            double threshold = getThreshold(allPosts.get(i).getChain(), allPosts);
            if (threshold < maxThreshold) {
                CategoryData category = new CategoryData();

                Map<PostData, Double> postMap = new HashMap<>();
                postMap.put(allPosts.get(i), 0.0);
                for (int i2 = 0; i2 < allPosts.size(); i2++) {
                    if (i != i2) {
                        Double difference = allPosts.get(i).getChain().getDifference(allPosts.get(i2).getChain());
                        if (difference != null && difference < threshold)
                            postMap.put(allPosts.get(i2), difference);
                    }
                }

                List<PostData> posts = new ArrayList<>(postMap.keySet());
                if (posts.size() > 1) {
                    Collections.sort(posts, (p1, p2) -> (int) ((postMap.get(p2) - postMap.get(p1)) * 100));
                    category.setPosts(posts);

                    SortOfAMarkovChainOrSomething base = posts.get(0).getChain();
                    category.setAverages(SortOfAMarkovChainOrSomething.getWordAverages(base, posts.get(1).getChain()));
                    for (int i2 = 2; i2 < posts.size() && category.averages.size() < 2; i2++)
                        category.averages.addAll(SortOfAMarkovChainOrSomething.getWordAverages(base, posts.get(i2).getChain()));

                    categories.add(category);
                }
            }
        }

        Collections.sort(categories);
        return categories;
    }

    private static double getThreshold(SortOfAMarkovChainOrSomething base, List<PostData> posts) {
        double threshold = 0;
        for (int i = 0; i < posts.size(); i++) {
            if (!posts.get(i).equals(base.getPost())) {
                Double difference = posts.get(i).getChain().getDifference(base);
                threshold = ((threshold * i) + (difference != null ? difference : threshold)) / (i + 1);
            }
        }

        return threshold;
    }

    @Override
    public int compareTo(CategoryData categoryData) {
        return categoryData.getDescription(-1).length() - getDescription(-1).length();
    }
}
