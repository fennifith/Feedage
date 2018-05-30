package me.jfenn.feedage.utils;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import me.jfenn.feedage.lib.data.FeedData;
import me.jfenn.feedage.lib.data.PostData;

public class PreferenceUtils {

    public static SharedPreferences.Editor putPostList(SharedPreferences.Editor editor, FeedData parent) {
        List<PostData> posts = parent.getPosts();
        editor.putInt("feedposts-" + parent.getBasicHomepage() + "-length", posts.size());
        for (int i = 0; i < posts.size(); i++) {
            PostData post = posts.get(i);
            editor = editor.putString("feedposts-" + parent.getBasicHomepage() + "-" + i, post.toString());
        }

        return editor;
    }

    public static List<PostData> getPostList(SharedPreferences prefs, FeedData parent) {
        List<PostData> posts = new ArrayList<>();
        int length = prefs.getInt("feedposts-" + parent.getBasicHomepage() + "-length", 0);
        for (int i = 0; i < length; i++) {
            PostData post = PostData.fromString(parent, prefs.getString("feedposts-" + parent.getBasicHomepage() + "-" + i, null));
            if (post != null)
                posts.add(post);
        }

        return posts;
    }

}
