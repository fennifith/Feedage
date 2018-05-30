package me.jfenn.feedage.utils;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import me.jfenn.feedage.data.PostParcelData;
import me.jfenn.feedage.lib.data.PostData;

public class PreferenceUtils {

    public static SharedPreferences.Editor putPostList(SharedPreferences.Editor editor, String name, List<PostData> posts) {
        editor.putInt(name + "-length", posts.size());
        for (int i = 0; i < posts.size(); i++)
            editor = new PostParcelData(posts.get(i)).putPreference(editor, name + "-" + i + "-");

        return editor;
    }

    public static List<PostData> getPostList(SharedPreferences prefs, String name) {
        List<PostData> posts = new ArrayList<>();
        int length = prefs.getInt(name + "-length", 0);
        for (int i = 0; i < length; i++)
            posts.add(new PostParcelData(prefs, name + "-" + i + "-").getPost());

        return posts;
    }

}
