package me.jfenn.feedage.activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.jfenn.feedage.R;
import me.jfenn.feedage.adapters.ItemAdapter;
import me.jfenn.feedage.data.PostParcelData;
import me.jfenn.feedage.data.items.AuthorItemData;
import me.jfenn.feedage.data.items.ItemData;
import me.jfenn.feedage.lib.data.AuthorData;
import me.jfenn.feedage.lib.data.FeedData;
import me.jfenn.feedage.lib.data.PostData;

public class PostActivity extends AppCompatActivity {

    public static final String EXTRA_POST_PARCEL = "me.jfenn.feedage.EXTRA_POST_PARCEL";

    private Toolbar toolbar;
    private RecyclerView authors;
    private TextView content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        PostParcelData parcel = getIntent().getParcelableExtra(EXTRA_POST_PARCEL);
        PostData post = parcel.getPost();
        FeedData parent = post.getParent();

        toolbar = findViewById(R.id.toolbar);
        authors = findViewById(R.id.authors);
        content = findViewById(R.id.content);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitle(post.getTitle());

        String html = post.getHTML();
        content.setText(html != null ? Html.fromHtml(html) : null);

        List<ItemData> items = new ArrayList<>();
        for (AuthorData author : post.getAuthors())
            items.add(new AuthorItemData(author));

        authors.setLayoutManager(new LinearLayoutManager(this));
        authors.setAdapter(new ItemAdapter(items));

        int backgroundColor = parent.getBackgroundColor(),
                darkBackgroundColor = Color.rgb(Math.max(0, Color.red(backgroundColor) - 30), Math.max(0, Color.green(backgroundColor) - 30), Math.max(0, Color.blue(backgroundColor) - 30)),
                textColor = parent.getTextColor(),
                secondaryTextColor = Color.argb(150, Color.red(textColor), Color.green(textColor), Color.blue(textColor));

        findViewById(android.R.id.content).setBackgroundColor(backgroundColor);
        content.setTextColor(secondaryTextColor);
        content.setLinkTextColor(textColor);
        toolbar.setTitleTextColor(textColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(darkBackgroundColor);
    }
}
