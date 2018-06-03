package me.jfenn.feedage.utils;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import me.jfenn.feedage.data.CategoryParcelData;
import me.jfenn.feedage.data.FeedParcelData;
import me.jfenn.feedage.data.PostParcelData;
import me.jfenn.feedage.lib.data.CategoryData;
import me.jfenn.feedage.lib.data.FeedData;
import me.jfenn.feedage.lib.data.PostData;

public class PreferenceUtils {

    public static SharedPreferences.Editor putPostList(SharedPreferences.Editor editor, String name, List<PostData> posts) {
        editor = editor.putInt(name + "-length", posts.size());
        for (int i = 0; i < posts.size(); i++)
            editor = new PostParcelData(posts.get(i)).putPreference(editor, name + "-" + i);

        return editor;
    }

    public static List<PostData> getPostList(SharedPreferences prefs, String name) {
        List<PostData> posts = new ArrayList<>();
        int length = prefs.getInt(name + "-length", 0);
        for (int i = 0; i < length; i++)
            posts.add(new PostParcelData(prefs, name + "-" + i).getPost());

        return posts;
    }

    public static SharedPreferences.Editor putCategoryList(SharedPreferences.Editor editor, String name, List<CategoryData> categories) {
        editor = editor.putInt(name + "-length", categories.size());
        for (int i = 0; i < categories.size(); i++)
            editor = new CategoryParcelData(categories.get(i)).putPreference(editor, name + "-" + i);

        return editor;
    }

    public static List<CategoryData> getCategoryList(SharedPreferences prefs, String name) {
        List<CategoryData> categories = new ArrayList<>();
        int length = prefs.getInt(name + "-length", 0);
        for (int i = 0; i < length; i++)
            categories.add(new CategoryParcelData(prefs, name + "-" + i).getCategory());

        return categories;
    }

    public static SharedPreferences.Editor putFeedList(SharedPreferences.Editor editor, String name, List<FeedData> feeds) {
        editor = editor.putInt(name + "-length", feeds.size());
        for (int i = 0; i < feeds.size(); i++)
            editor = new FeedParcelData(feeds.get(i)).putPreference(editor, name + "-" + i);

        return editor;
    }

    public static List<FeedData> getFeedList(SharedPreferences prefs, String name) {
        List<FeedData> feeds = new ArrayList<>();
        int length = prefs.getInt(name + "-length", 0);
        for (int i = 0; i < length; i++)
            feeds.add(new FeedParcelData(prefs, name + "-" + i).getFeed());

        return feeds;
    }

    public static SharedPreferences.Editor putStringList(SharedPreferences.Editor editor, String name, List<String> strings) {
        editor = editor.putInt(name + "-length", strings.size());
        for (int i = 0; i < strings.size(); i++)
            editor = editor.putString(name + "-" + i, strings.get(i));

        return editor;
    }

    public static List<String> getStringList(SharedPreferences prefs, String name) {
        List<String> strings = new ArrayList<>();
        int length = prefs.getInt(name + "-length", 0);
        for (int i = 0; i < length; i++)
            strings.add(prefs.getString(name + "-" + i, null));

        return strings;
    }
}
