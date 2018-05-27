package me.jfenn.feedage.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.jfenn.feedage.R;
import me.jfenn.feedage.adapters.ItemAdapter;
import me.jfenn.feedage.data.PostParcelData;
import me.jfenn.feedage.data.items.AuthorItemData;
import me.jfenn.feedage.data.items.ItemData;
import me.jfenn.feedage.lib.data.AuthorData;
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

        toolbar = findViewById(R.id.toolbar);
        authors = findViewById(R.id.authors);
        content = findViewById(R.id.content);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitle(post.getTitle());
        content.setText(post.getContentText());

        List<ItemData> items = new ArrayList<>();
        for (AuthorData author : post.getAuthors())
            items.add(new AuthorItemData(author));

        authors.setLayoutManager(new LinearLayoutManager(this));
        authors.setAdapter(new ItemAdapter(items));

        findViewById(android.R.id.content).setBackgroundColor(post.getParent().getBackgroundColor());
        content.setTextColor(post.getParent().getTextColor());
    }
}
