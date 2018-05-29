package me.jfenn.feedage.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
    }
}
