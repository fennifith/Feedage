package me.jfenn.feedage.utils;

public class StringUtils {

    public static String toPlainText(String html) {
        return html.replaceAll("<.*?>", "");
    }

}
