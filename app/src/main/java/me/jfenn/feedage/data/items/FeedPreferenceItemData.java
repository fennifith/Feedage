package me.jfenn.feedage.data.items;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import me.jfenn.feedage.R;
import me.jfenn.feedage.lib.data.FeedData;
import me.jfenn.feedage.views.ColorView;

public class FeedPreferenceItemData extends ItemData<FeedPreferenceItemData.ViewHolder> {

    private FeedData feed;

    public FeedPreferenceItemData(FeedData feed) {
        super(R.layout.item_feed_preference);
        this.feed = feed;
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public void bind(Context context, ViewHolder viewHolder) {
        viewHolder.url.setText(feed.getUrl());

        viewHolder.textColor.setColor(feed.getTextColor());
        viewHolder.textColor.setOnClickListener(v -> {

        });

        viewHolder.backgroundColor.setColor(feed.getBackgroundColor());
        viewHolder.backgroundColor.setOnClickListener(v -> {

        });
    }

    public static class ViewHolder extends ItemData.ViewHolder {

        private TextView url;
        private ColorView textColor;
        private ColorView backgroundColor;

        public ViewHolder(View v) {
            super(v);
            url = v.findViewById(R.id.url);
            textColor = v.findViewById(R.id.textColor);
            backgroundColor = v.findViewById(R.id.backgroundColor);
        }
    }

}
