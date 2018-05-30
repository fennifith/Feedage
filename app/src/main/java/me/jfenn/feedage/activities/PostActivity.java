package me.jfenn.feedage.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import me.jfenn.feedage.Feedage;
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
    private TextView title;
    private TextView source;
    private TextView content;
    private ImageView image;

    private PostData post;
    private FeedData parent;
    private Feedage feedage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        feedage = (Feedage) getApplicationContext();

        PostParcelData parcel = getIntent().getParcelableExtra(EXTRA_POST_PARCEL);
        post = parcel.getPost();
        parent = post.getParent();

        toolbar = findViewById(R.id.toolbar);
        authors = findViewById(R.id.authors);
        title = findViewById(R.id.title);
        source = findViewById(R.id.source);
        content = findViewById(R.id.content);
        image = findViewById(R.id.image);

        toolbar.setTitle(post.getTitle());

        if (post.getImageUrl() != null) {
            image.setVisibility(View.VISIBLE);
            Glide.with(this).load(post.getImageUrl()).into(image);
            image.setOnClickListener(v -> {
                ActivityOptionsCompat options;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    options = ActivityOptionsCompat.makeSceneTransitionAnimation(PostActivity.this,
                            new Pair<>(image, ViewCompat.getTransitionName(image)));
                } else {
                    options = ActivityOptionsCompat.makeClipRevealAnimation(image, (int) image.getX(),
                            (int) image.getY(), image.getWidth(), image.getHeight());
                }

                Intent intent = new Intent(v.getContext(), ImageViewActivity.class);
                intent.putExtra(ImageViewActivity.EXTRA_IMAGE_URL, post.getImageUrl());
                v.getContext().startActivity(intent, options.toBundle());
            });
        } else image.setVisibility(View.GONE);

        title.setText(post.getTitle());
        source.setText("From " + post.getParent().getBasicHomepage());
        source.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://" + post.getParent().getBasicHomepage()))));

        String html = post.getHTML();
        content.setMovementMethod(new LinkMovementMethod());
        new HtmlParserThread(html, html1 -> content.setText(html1)).start();

        List<ItemData> items = new ArrayList<>();
        for (AuthorData author : post.getAuthors())
            items.add(new AuthorItemData(author));

        authors.setLayoutManager(new LinearLayoutManager(this));
        authors.setAdapter(new ItemAdapter(items));

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post, menu);
        menu.findItem(R.id.bookmark).setIcon(feedage.isBookmarked(post) ? R.drawable.ic_bookmark : R.drawable.ic_bookmark_outline);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        else if (item.getItemId() == R.id.bookmark) {
            boolean isBookmarked = !feedage.isBookmarked(post);
            feedage.setBookmarked(post, isBookmarked);
            item.setIcon(isBookmarked ? R.drawable.ic_bookmark : R.drawable.ic_bookmark_outline);
        } else if (item.getItemId() == R.id.open)
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

    private static class HtmlParserThread extends Thread {

        private WeakReference<HtmlListener> listenerReference;
        private String source;

        public HtmlParserThread(String source, HtmlListener listener) {
            listenerReference = new WeakReference<>(listener);
            this.source = source;
        }

        @Override
        public void run() {
            Spanned html = Html.fromHtml(source);
            new Handler(Looper.getMainLooper()).post(() -> {
                HtmlListener listener = listenerReference.get();
                if (listener != null)
                    listener.onHtml(html);
            });
        }

        public interface HtmlListener {
            void onHtml(Spanned html);
        }
    }
}
