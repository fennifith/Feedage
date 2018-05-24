package me.jfenn.feedage.lib.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.jfenn.feedage.lib.utils.SortOfAMarkovChainOrSomething;

public class CategoryData implements Comparable<CategoryData> {

    private String title;
    private List<PostData> posts;
    private List<SortOfAMarkovChainOrSomething.WordAverage> averages;

    public CategoryData() {
        posts = new ArrayList<>();
        averages = new ArrayList<>();
    }

    void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
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
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 10 && i < averages.size(); i++)
            builder.append(averages.get(i));

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
                    category.setTitle(posts.get(0).getTitle());

                    category.setAverages(SortOfAMarkovChainOrSomething.getWordAverages(posts.get(0).getChain(), posts.get(1).getChain()));
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
        return averages.size() - categoryData.averages.size();
    }
}
