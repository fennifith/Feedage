package me.jfenn.feedage.lib.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.jfenn.feedage.lib.data.PostData;

public class SortOfAMarkovChainOrSomething {

    private static final List<String> FORBIDDEN_WORDS = Arrays.asList(
            "a",
            "href",
            "rel",
            "width",
            "height",
            "srcset",
            "src",
            "alt",
            "class",
            "style",
            "url",
            "title",
            "wp",
            "size",
            "http",
            "https",
            "article",
            "header",
            "footer",
            "property",
            "content",
            "target",
            "frameborder",
            "allowfullscreen",
            "nofollow",
            "async",
            "charset"
    );

    private PostData post;
    private List<WordAverage> averages;

    public SortOfAMarkovChainOrSomething(PostData post) {
        this.post = post;
        averages = new ArrayList<>();

        addContent(post.getTitle());
        addContent(post.getDescription());
        addContent(post.getContent());
        for (String string : post.getTags())
            addContent(string);

        for (WordAverage average : averages)
            average.getYes();
    }

    public PostData getPost() {
        return post;
    }

    private void addContent(String content) {
        if (content == null || content.length() < 1)
            return;

        List<String> list = new ArrayList<>(Arrays.asList(content.split(" ")));
        for (int i = 0; i < list.size(); i++) {
            String word = WordAverage.toPlainText(list.get(i));
            if (FORBIDDEN_WORDS.contains(word))
                list.remove(i--);
        }

        for (int i = 1; i < list.size(); i++) {
            WordAverage average = new WordAverage(list.get(i - 1), list.get(i));
            if (average.isValid()) {
                if (averages.contains(average))
                    averages.get(averages.indexOf(average)).onThingHappen();
                else averages.add(average);
            }
        }
    }

    public Double getDifference(SortOfAMarkovChainOrSomething o) {
        double difference = 0;
        double divisor = Math.max(1, Math.abs(averages.size() - o.averages.size()));
        int count = 0;
        for (WordAverage average : averages) {
            if (o.averages.contains(average)) {
                difference = (double) Math.abs(o.averages.get(o.averages.indexOf(average)).getYes() - average.getYes()) / divisor;
                count++;
            } else difference += 1;
        }

        return count > 0 ? difference / count : null;
    }

    public static List<WordAverage> getWordAverages(SortOfAMarkovChainOrSomething o1, SortOfAMarkovChainOrSomething o2) {
        List<WordAverage> averages = new ArrayList<>();
        for (WordAverage average : o1.averages) {
            if (o2.averages.contains(average)) {
                WordAverage newAverage = new WordAverage(average.getWord1(), average.getWord2());
                newAverage.yes = average.getYes() + o2.averages.get(o2.averages.indexOf(average)).getYes();
                averages.add(newAverage);
            }
        }

        Collections.sort(averages);
        return averages;
    }

    public static class WordAverage implements Comparable<WordAverage> {

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
            return word1 != null && word1.length() > 4
                    && word2 != null && word2.length() > 4
                    && !word1.equals(word2);
        }

        public int getYes() {
            return yes;
        }

        private void onThingHappen() {
            yes++;
        }

        @Override
        public String toString() {
            return "(" + word1 + ", " + word2 + ") ";
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

        @Override
        public int compareTo(WordAverage wordAverage) {
            return yes - wordAverage.yes;
        }
    }

}
