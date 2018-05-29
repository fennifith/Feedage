package me.jfenn.feedage.activities;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import me.jfenn.feedage.R;

public class ImageViewActivity extends AppCompatActivity {

    public static final String EXTRA_IMAGE_URL = "me.jfenn.feedage.EXTRA_IMAGE_URL";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        ImageView image = findViewById(R.id.image);
        String url = getIntent().getStringExtra(EXTRA_IMAGE_URL);
        if (url != null)
            Glide.with(this).load(url).into(image);

        Toolbar toolbar = findViewById(R.id.toolbar);
        Drawable icon = VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back, getTheme());
        DrawableCompat.setTint(icon, Color.WHITE);
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
