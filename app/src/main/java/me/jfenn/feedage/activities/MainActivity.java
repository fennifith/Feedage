package me.jfenn.feedage.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import me.jfenn.attribouter.Attribouter;
import me.jfenn.feedage.Feedage;
import me.jfenn.feedage.R;
import me.jfenn.feedage.fragments.BaseFragment;
import me.jfenn.feedage.fragments.BookmarksFragment;
import me.jfenn.feedage.fragments.CategoriesFragment;
import me.jfenn.feedage.fragments.FeedsFragment;
import me.jfenn.feedage.lib.FeedageLib;
import me.jfenn.feedage.lib.data.CategoryData;
import me.jfenn.feedage.lib.data.FeedData;
import me.jfenn.feedage.views.TintedImageView;

public class MainActivity extends AppCompatActivity implements FeedageLib.OnCategoriesUpdatedListener, FragmentManager.OnBackStackChangedListener {

    private Feedage feedage;
    private BaseFragment fragment;

    private TintedImageView home;
    private TintedImageView feeds;
    private TintedImageView bookmarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        feedage = (Feedage) getApplicationContext();

        if (savedInstanceState == null) {
            fragment = new CategoriesFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment, fragment)
                    .commit();
        } else {
            if (fragment == null)
                fragment = new CategoriesFragment();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, fragment)
                    .commit();
        }

        getSupportFragmentManager().addOnBackStackChangedListener(this);
        feedage.addListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        home = findViewById(R.id.home);
        feeds = findViewById(R.id.feeds);
        bookmarks = findViewById(R.id.bookmarks);

        setSupportActionBar(toolbar);

        home.setTint(ContextCompat.getColor(this, R.color.colorAccent));
        home.setOnClickListener(v -> {
            if (!(fragment instanceof CategoriesFragment)) {
                fragment = new CategoriesFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, fragment)
                        .commit();

                home.tint(ContextCompat.getColor(this, R.color.colorAccent));
                feeds.tint(Color.BLACK);
                bookmarks.tint(Color.BLACK);
            }

            AnimatedVectorDrawableCompat animatedVector = AnimatedVectorDrawableCompat.create(v.getContext(), R.drawable.ic_anim_home_flip);
            if (animatedVector != null) {
                home.setImageDrawable(animatedVector);
                animatedVector.start();
            }
        });

        feeds.setTint(Color.BLACK);
        feeds.setOnClickListener(v -> {
            if (!(fragment instanceof FeedsFragment)) {
                fragment = new FeedsFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, fragment)
                        .commit();

                home.tint(Color.BLACK);
                feeds.tint(ContextCompat.getColor(this, R.color.colorAccent));
                bookmarks.tint(Color.BLACK);
            }

            AnimatedVectorDrawableCompat animatedVector = AnimatedVectorDrawableCompat.create(v.getContext(), R.drawable.ic_anim_feed_flip);
            if (animatedVector != null) {
                feeds.setImageDrawable(animatedVector);
                animatedVector.start();
            }
        });

        bookmarks.setTint(Color.BLACK);
        bookmarks.setOnClickListener(v -> {
            if (!(fragment instanceof BookmarksFragment)) {
                fragment = new BookmarksFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, fragment)
                        .commit();

                home.tint(Color.BLACK);
                feeds.tint(Color.BLACK);
                bookmarks.tint(ContextCompat.getColor(this, R.color.colorAccent));
            }

            AnimatedVectorDrawableCompat animatedVector = AnimatedVectorDrawableCompat.create(v.getContext(), R.drawable.ic_anim_bookmark_flip);
            if (animatedVector != null) {
                bookmarks.setImageDrawable(animatedVector);
                animatedVector.start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        feedage.removeListener(this);
    }

    @Override
    public void onFeedsUpdated(List<FeedData> feeds) {

    }

    @Override
    public void onCategoriesUpdated(List<CategoryData> categories) {

    }

    @Override
    public void onBackStackChanged() {
        fragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_about)
            Attribouter.from(this).show();

        return super.onOptionsItemSelected(item);
    }
}
