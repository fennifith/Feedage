package me.jfenn.feedage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import me.jfenn.feedage.lib.Feedage;
import me.jfenn.feedage.lib.data.AtomFeedData;
import me.jfenn.feedage.lib.data.CategoryData;

public class MainActivity extends AppCompatActivity implements Feedage.OnCategoriesUpdatedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Feedage feedage = new Feedage(
                new AtomFeedData("https://www.androidpolice.com/feed/?paged=%s", 1),
                new AtomFeedData("https://www.androidauthority.com/feed/?paged=%s", 1),
                new AtomFeedData("https://www.theverge.com/rss/index.xml"),
                new AtomFeedData("https://techaeris.com/feed/?paged=%s", 1)
        );

        feedage.setListener(this);
        feedage.getNext();
    }

    @Override
    public void onCategoriesUpdated(List<CategoryData> categories) {

    }
}
