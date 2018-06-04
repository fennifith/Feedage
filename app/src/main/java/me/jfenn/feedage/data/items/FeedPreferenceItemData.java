package me.jfenn.feedage.data.items;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import james.colorpickerdialog.dialogs.ColorPickerDialog;
import james.colorpickerdialog.dialogs.PreferenceDialog;
import me.jfenn.feedage.R;
import me.jfenn.feedage.lib.data.FeedData;
import me.jfenn.feedage.utils.ColorUtils;
import me.jfenn.feedage.views.ColorView;

public class FeedPreferenceItemData extends ItemData<FeedPreferenceItemData.ViewHolder> {

    private FeedData feed;

    public FeedPreferenceItemData(FeedData feed) {
        super(R.layout.item_feed_preference);
        this.feed = feed;
    }

    public FeedData getFeed() {
        return feed;
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public void bind(Context context, ViewHolder viewHolder) {
        viewHolder.url.setText(feed.getUrl());

        viewHolder.backgroundColor.setColor(feed.getBackgroundColor());
        viewHolder.backgroundColor.setOnClickListener(v -> new ColorPickerDialog(v.getContext()).setPreference(feed.getBackgroundColor()).setDefaultPreference(Color.WHITE).setListener(new PreferenceDialog.OnPreferenceListener<Integer>() {
            @Override
            public void onPreference(PreferenceDialog dialog, Integer preference) {
                feed.setBackgroundColor(preference);
                feed.setTextColor(ColorUtils.isColorDark(preference) ? Color.WHITE : Color.BLACK);
                viewHolder.backgroundColor.setColor(preference);
            }

            @Override
            public void onCancel(PreferenceDialog dialog) {

            }
        }).show());
    }

    public static class ViewHolder extends ItemData.ViewHolder {

        private TextView url;
        private ColorView backgroundColor;

        public ViewHolder(View v) {
            super(v);
            url = v.findViewById(R.id.url);
            backgroundColor = v.findViewById(R.id.backgroundColor);
        }
    }

}
