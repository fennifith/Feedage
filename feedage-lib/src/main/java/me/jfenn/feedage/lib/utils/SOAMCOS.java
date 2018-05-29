package me.jfenn.feedage.lib.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.jfenn.feedage.lib.data.PostData;

public class SOAMCOS { //stands for "Sort Of A Markov Chain Or Something" because the class name was too long and I didn't know what else to call it

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

    public SOAMCOS(PostData post) {
        this.post = post;
        averages = new ArrayList<>();

        addContent(post.getTitle());
        addContent(post.getDescription());
        addContent(post.getContentText());
        for (String string : post.getTags())
            addContent(string);

        for (WordAverage average : averages)
            average.getCount();
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
                    averages.get(averages.indexOf(average)).onCounted();
                else averages.add(average);
            }
        }
    }

    public Double getDifference(SOAMCOS o) {
        double difference = 0;
        double divisor = Math.max(1, Math.abs(averages.size() - o.averages.size()));
        int count = 0;
        for (WordAverage average : averages) {
            if (o.averages.contains(average)) {
                difference = (double) Math.abs(o.averages.get(o.averages.indexOf(average)).getCount() - average.getCount()) / divisor;
                count++;
            } else difference += 1;
        }

        return count > 0 ? difference / count : null;
    }

    public static List<WordAverage> getWordAverages(SOAMCOS o1, SOAMCOS o2) {
        List<WordAverage> averages = new ArrayList<>();
        for (WordAverage average : o1.averages) {
            if (o2.averages.contains(average)) {
                WordAverage newAverage = new WordAverage(average.getFirstWord(), average.getLastWord());
                newAverage.count = average.getCount() + o2.averages.get(o2.averages.indexOf(average)).getCount();
                averages.add(newAverage);
            }
        }

        Collections.sort(averages);
        return averages;
    }

    public static class WordAverage implements Comparable<WordAverage> {

        private int count;
        private String firstWord, lastWord;

        public WordAverage(String firstWord, String lastWord) {
            this.firstWord = firstWord != null ? toPlainText(firstWord) : null;
            this.lastWord = lastWord != null ? toPlainText(lastWord) : null;
            onCounted();
        }

        public String getFirstWord() {
            return firstWord;
        }

        public String getLastWord() {
            return lastWord;
        }

        public boolean isValid() {
            return firstWord != null && firstWord.length() > 4
                    && lastWord != null && lastWord.length() > 4
                    && !firstWord.equals(lastWord);
        }

        public int getCount() {
            return count;
        }

        private void onCounted() {
            count++;
        }

        @Override
        public String toString() {
            return "(" + firstWord + ", " + lastWord + ") ";
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof WordAverage) {
                WordAverage average = (WordAverage) o;
                return compareString(firstWord, average.firstWord) && compareString(lastWord, average.lastWord);
            } else return super.equals(o);
        }

        @Override
        public int compareTo(WordAverage wordAverage) {
            return count - wordAverage.count;
        }

        private static String toPlainText(String word) {
            word = word.toLowerCase();

            Pattern pattern = Pattern.compile("([A-Za-z]*)");
            Matcher matcher = pattern.matcher(word);

            if (matcher.find())
                word = matcher.group();

            return word.replaceAll("<.*?>", "");
        }

        private static boolean compareString(String s1, String s2) {
            if (s1 == null || s2 == null || s1.length() == 0 || s2.length() == 0)
                return false;
            else if (s1.length() == s2.length())
                return s1.equals(s2);
            else {
                if (s1.length() > s2.length())
                    return s2.indexOf(s1) == 0;
                else return s2.indexOf(s1) == 0;
            }
        }
    }

}
