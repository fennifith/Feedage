package me.jfenn.feedage.lib.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.jfenn.feedage.lib.data.PostData;

public class SortOfAMarkovChainOrSomething {

    private PostData post;
    private List<WordAverage> averages;

    public SortOfAMarkovChainOrSomething(PostData post) {
        this.post = post;
        averages = new ArrayList<>();

        addContent(post.getTitle());
        addContent(post.getDescription());
        addContent(post.getContent());

        for (WordAverage average : averages)
            average.getYes();
    }

    public PostData getPost() {
        return post;
    }

    private void addContent(String content) {
        if (content == null || content.length() < 1)
            return;

        String[] arr = content.split(" ");
        for (int i = 1; i < arr.length; i++) {
            WordAverage average = new WordAverage(arr[i - 1], arr[i]);
            if (average.isValid()) {
                if (averages.contains(average))
                    averages.get(averages.indexOf(average)).onThingHappen();
                else averages.add(average);
            }
        }
    }

    public double getDifference(SortOfAMarkovChainOrSomething o) {
        double difference = 0;
        double divisor = Math.max(1, Math.abs(averages.size() - o.averages.size()));
        for (WordAverage average : averages) {
            if (o.averages.contains(average)) {
                difference = (double) Math.abs(o.averages.get(o.averages.indexOf(average)).getYes() - average.getYes()) / divisor;
            } else difference += 1;
        }

        return difference;
    }

    public static class WordAverage {

        private int yes;
        private String word1, word2;

        public WordAverage(String word1, String word2) {
            this.word1 = word1 != null ? toPlainText(word1) : null;
            this.word2 = word2 != null ? toPlainText(word2) : null;
            onThingHappen();
        }

        public String getWord1() {
            return word1;
        }

        public String getWord2() {
            return word2;
        }

        public boolean isValid() {
            return word1 != null && word1.length() > 0
                    && word2 != null && word2.length() > 0;
        }

        public int getYes() {
            return yes;
        }

        private void onThingHappen() {
            yes++;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof WordAverage) {
                WordAverage average = (WordAverage) o;
                return word1 != null && word1.length() > 0 && word1.equals(average.word1)
                        && word2 != null && word2.length() > 0 && word2.equals(average.word2);
            } else return super.equals(o);
        }

        private static String toPlainText(String word) {
            word = word.toLowerCase();

            Pattern pattern = Pattern.compile("([A-Za-z]*)");
            Matcher matcher = pattern.matcher(word);

            if (matcher.find())
                word = matcher.group();

            return word.replaceAll("<.*?>", "");
        }
    }

}
