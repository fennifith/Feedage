package me.jfenn.feedage.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

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
    private ImageView image;

    private PostData post;
    private FeedData parent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        PostParcelData parcel = getIntent().getParcelableExtra(EXTRA_POST_PARCEL);
        post = parcel.getPost();
        parent = post.getParent();

        toolbar = findViewById(R.id.toolbar);
        authors = findViewById(R.id.authors);
        content = findViewById(R.id.content);
        image = findViewById(R.id.image);

        toolbar.setTitle(post.getTitle());

        if (post.getImageUrl() != null) {
            image.setVisibility(View.VISIBLE);
            Glide.with(this).load(post.getImageUrl()).into(image);
            image.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), ImageViewActivity.class);
                intent.putExtra(ImageViewActivity.EXTRA_IMAGE_URL, post.getImageUrl());
                v.getContext().startActivity(intent);
            });
        } else image.setVisibility(View.GONE);

        String html = post.getHTML();
        content.setText(html != null ? Html.fromHtml(html) : null);
        content.setMovementMethod(new LinkMovementMethod());

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
        content.setHintTextColor(textColor);
        toolbar.setTitleTextColor(textColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(darkBackgroundColor);
            getWindow().setNavigationBarColor(darkBackgroundColor);
        }

        Drawable drawable = VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back, getTheme());
        DrawableCompat.setTint(drawable, textColor);
        toolbar.setNavigationIcon(drawable);

        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        else if (item.getItemId() == R.id.bookmark)
            ;
        else if (item.getItemId() == R.id.open)
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(post.getSourceUrl())));
        else if (item.getItemId() == R.id.share) {
            Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse(post.getSourceUrl()));
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, post.getTitle());
            intent.putExtra(Intent.EXTRA_TEXT, post.getSourceUrl());
            startActivity(Intent.createChooser(intent, getString(R.string.title_share)));
        }

        return super.onOptionsItemSelected(item);
    }
}
