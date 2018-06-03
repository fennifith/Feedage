package me.jfenn.feedage.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import me.jfenn.feedage.Feedage;
import me.jfenn.feedage.R;
import me.jfenn.feedage.adapters.ItemAdapter;
import me.jfenn.feedage.data.items.ItemData;
import me.jfenn.feedage.data.items.PostItemData;
import me.jfenn.feedage.lib.data.AuthorData;
import me.jfenn.feedage.lib.data.CategoryData;
import me.jfenn.feedage.lib.data.FeedData;
import me.jfenn.feedage.lib.data.PostData;

public class PostsActivity extends AppCompatActivity {

    public static final String EXTRA_CATEGORY = "me.jfenn.feedage.EXTRA_CATEGORY";
    public static final String EXTRA_FEED = "me.jfenn.feedage.EXTRA_FEED";
    public static final String EXTRA_AUTHOR = "me.jfenn.feedage.EXTRA_AUTHOR";

    private Feedage feedage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        feedage = (Feedage) getApplicationContext();

        Toolbar toolbar = findViewById(R.id.toolbar);
        RecyclerView recycler = findViewById(R.id.recycler);

        List<ItemData> items = new ArrayList<>();

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_CATEGORY)) {
            CategoryData category = null;
            String categoryName = intent.getStringExtra(EXTRA_CATEGORY);
            for (CategoryData category1 : feedage.getCategories()) {
                if (category1.getTitle().equals(categoryName)) {
                    category = category1;
                    break;
                }
            }

            if (category != null) {
                toolbar.setTitle(categoryName);
                for (PostData post : category.getPosts())
                    items.add(new PostItemData(post, R.layout.item_post_horiz));
            } else finish();
        } else if (intent.hasExtra(EXTRA_AUTHOR) && intent.hasExtra(EXTRA_FEED)) {
            FeedData feed = null;
            String feedName = intent.getStringExtra(EXTRA_FEED);
            String authorName = intent.getStringExtra(EXTRA_AUTHOR);
            for (FeedData feed1 : feedage.getFeeds()) {
                if (feed1.getUrl().equals(feedName)) {
                    feed = feed1;
                    break;
                }
            }

            if (feed != null) {
                toolbar.setTitle(String.format(getString(R.string.title_written_by_author), authorName));
                for (PostData post : feed.getPosts()) {
                    boolean hasAuthor = false;
                    for (AuthorData author : post.getAuthors()) {
                        if (author.getName().equals(authorName)) {
                            hasAuthor = true;
                            break;
                        }
                    }

                    if (hasAuthor)
                        items.add(new PostItemData(post, R.layout.item_post_horiz));
                }
            } else finish();
        } else if (intent.hasExtra(EXTRA_FEED)) {
            FeedData feed = null;
            String feedName = intent.getStringExtra(EXTRA_FEED);
            for (FeedData feed1 : feedage.getFeeds()) {
                if (feed1.getUrl().equals(feedName)) {
                    feed = feed1;
                    break;
                }
            }

            if (feed != null) {
                toolbar.setTitle(feed.getName() != null ? feed.getName() : feed.getBasicHomepage());
                for (PostData post : feed.getPosts())
                    items.add(new PostItemData(post, R.layout.item_post_horiz));
            } else finish();
        }

        recycler.setLayoutManager(new GridLayoutManager(this, 3));
        recycler.setAdapter(new ItemAdapter(items));

        Drawable icon = VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back, getTheme());
        DrawableCompat.setTint(icon, Color.BLACK);
        toolbar.setNavigationIcon(icon);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }
}
