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

    void addAverage(SortOfAMarkovChainOrSomething.WordAverage average) {
        if (!averages.contains(average)) {
            boolean isAdded = false;
            for (int i = 0; i < averages.size() && !isAdded; i++) {
                SortOfAMarkovChainOrSomething.WordAverage average1 = averages.get(i);
                if (average.getWord1().equals(average1.getWord2())) {
                    averages.add(i + 1, average);
                    isAdded = true;
                } else if (average.getWord2().equals(average1.getWord1())) {
                    averages.add(i, average);
                    isAdded = true;
                }
            }

            if (!isAdded)
                averages.add(average);
        }
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

    public String getDescriptionSentence() {
        StringBuilder builder = new StringBuilder();
        if (averages.size() > 1) {
            SortOfAMarkovChainOrSomething.WordAverage average = averages.get(0);
            String lastWord = average.getWord1();
            builder.append(lastWord.substring(0, 1).toUpperCase()).append(lastWord.substring(1)).append(" ");
            lastWord = average.getWord2();
            builder.append(lastWord);

            for (int i = 1; i < averages.size(); i++) {
                average = averages.get(i);
                if (lastWord.equals(average.getWord1())) {
                    builder.append(" ").append(average.getWord2());
                } else {
                    builder.append(". ").append(average.getWord1().substring(0, 1).toUpperCase()).append(average.getWord1().substring(1))
                            .append(" ").append(average.getWord2());
                }

                lastWord = average.getWord2();
            }

            builder.append(".");
        }
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
                    for (SortOfAMarkovChainOrSomething.WordAverage average : SortOfAMarkovChainOrSomething.getWordAverages(base, posts.get(1).getChain()))
                        category.addAverage(average);
                    for (int i2 = 2; i2 < posts.size() && category.averages.size() < 2; i2++) {
                        for (SortOfAMarkovChainOrSomething.WordAverage average : SortOfAMarkovChainOrSomething.getWordAverages(base, posts.get(i2).getChain()))
                            category.addAverage(average);
                    }

                    CategoryData equivalent = null;
                    for (CategoryData category2 : categories) {
                        for (int i2 = 0; i2 < category2.posts.size() && equivalent == null; i2++) {
                            PostData post = category2.posts.get(i2);
                            for (int i3 = 0; i3 < posts.size(); i3++) {
                                if (post.equals(posts.get(i3))) {
                                    equivalent = category2;
                                    break;
                                }
                            }
                        }

                        for (int i2 = 0; i2 < category2.averages.size() && equivalent == null; i2++) {
                            SortOfAMarkovChainOrSomething.WordAverage average = category2.averages.get(i2);
                            for (int i3 = 0; i3 < category.averages.size(); i3++) {
                                if (average.equals(category.averages.get(i3))) {
                                    equivalent = category2;
                                    break;
                                }
                            }
                        }

                        if (equivalent != null)
                            break;
                    }

                    if (equivalent != null) {
                        for (PostData post : posts) {
                            if (!equivalent.posts.contains(post))
                                equivalent.posts.add(post);
                        }

                        for (SortOfAMarkovChainOrSomething.WordAverage average : equivalent.averages)
                            equivalent.addAverage(average);
                    } else categories.add(category);
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
