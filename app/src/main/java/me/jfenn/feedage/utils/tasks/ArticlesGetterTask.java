package me.jfenn.feedage.utils.tasks;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.util.List;

import me.jfenn.feedage.Feedage;
import me.jfenn.feedage.lib.data.CategoryData;
import me.jfenn.feedage.lib.data.FeedData;
import me.jfenn.feedage.lib.data.PostData;
import me.jfenn.feedage.utils.PreferenceUtils;

public class ArticlesGetterTask extends AsyncTask {

    private Feedage feedage;
    private SharedPreferences prefs;
    private List<CategoryData> categories;

    public ArticlesGetterTask(Feedage feedage) {
        this.feedage = feedage;
        prefs = feedage.getPrefs();
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        categories = PreferenceUtils.getCategoryList(prefs, Feedage.PREF_CATEGORIES);
        feedage.onCategoriesUpdated(categories, false);

        for (FeedData feed : feedage.getFeeds()) {
            List<PostData> posts = PreferenceUtils.getPostList(prefs, Feedage.PREF_FEEDS + "-" + feed.getUrl() + "-posts");
            for (PostData post : posts)
                feed.addPost(post);
        }

        return null;
    }
}
