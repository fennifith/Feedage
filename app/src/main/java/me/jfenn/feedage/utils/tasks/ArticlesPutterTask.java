package me.jfenn.feedage.utils.tasks;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import me.jfenn.feedage.Feedage;
import me.jfenn.feedage.lib.data.FeedData;
import me.jfenn.feedage.utils.PreferenceUtils;

public class ArticlesPutterTask extends AsyncTask {

    private Feedage feedage;
    private SharedPreferences prefs;


    public ArticlesPutterTask(Feedage feedage) {
        this.feedage = feedage;
        prefs = feedage.getPrefs();
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        SharedPreferences.Editor editor = prefs.edit();
        editor = PreferenceUtils.putCategoryList(editor, Feedage.PREF_CATEGORIES, feedage.getCategories());

        for (FeedData feed : feedage.getFeeds())
            editor = PreferenceUtils.putPostList(editor, Feedage.PREF_FEEDS + "-" + feed.getUrl() + "-posts", feed.getPosts());

        editor.apply();
        return null;
    }
}
