package me.jfenn.feedage.activities;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import me.jfenn.attribouter.Attribouter;
import me.jfenn.feedage.Feedage;
import me.jfenn.feedage.R;
import me.jfenn.feedage.adapters.SimplePagerAdapter;
import me.jfenn.feedage.fragments.BookmarksFragment;
import me.jfenn.feedage.fragments.CategoriesFragment;
import me.jfenn.feedage.fragments.FeedsFragment;
import me.jfenn.feedage.lib.FeedageLib;
import me.jfenn.feedage.lib.data.CategoryData;
import me.jfenn.feedage.lib.data.FeedData;
import me.jfenn.feedage.views.TintedImageView;

public class MainActivity extends AppCompatActivity implements FeedageLib.OnCategoriesUpdatedListener, ViewPager.OnPageChangeListener {

    private Feedage feedage;

    private TintedImageView home;
    private TintedImageView feeds;
    private TintedImageView bookmarks;

    private ViewPager pager;
    private int textColor;
    private int accentColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        feedage = (Feedage) getApplicationContext();

        feedage.addListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        home = findViewById(R.id.home);
        feeds = findViewById(R.id.feeds);
        bookmarks = findViewById(R.id.bookmarks);
        pager = findViewById(R.id.pager);

        setSupportActionBar(toolbar);

        pager.setAdapter(new SimplePagerAdapter(getSupportFragmentManager(), new CategoriesFragment(), new FeedsFragment(), new BookmarksFragment()));
        pager.addOnPageChangeListener(this);

        textColor = ContextCompat.getColor(this, R.color.colorTextSecondary);
        accentColor = ContextCompat.getColor(this, R.color.colorAccent);

        home.setTint(accentColor);
        home.setOnClickListener(v -> {
            if (pager.getCurrentItem() != 0) {
                pager.setCurrentItem(0);
                onPageSelected(0);
            }
        });

        feeds.setTint(textColor);
        feeds.setOnClickListener(v -> {
            if (pager.getCurrentItem() != 1) {
                pager.setCurrentItem(1);
                onPageSelected(1);
            }
        });

        bookmarks.setTint(textColor);
        bookmarks.setOnClickListener(v -> {
            if (pager.getCurrentItem() != 2) {
                pager.setCurrentItem(2);
                onPageSelected(2);
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        home.tint(position == 0 ? accentColor : textColor);
        feeds.tint(position == 1 ? accentColor : textColor);
        bookmarks.tint(position == 2 ? accentColor : textColor);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
