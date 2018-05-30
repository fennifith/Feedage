package me.jfenn.feedage.data.items;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.jfenn.feedage.R;
import me.jfenn.feedage.adapters.ItemAdapter;
import me.jfenn.feedage.lib.data.FeedData;
import me.jfenn.feedage.lib.data.PostData;
import me.jfenn.feedage.utils.StringUtils;

public class FeedItemData extends ItemData<FeedItemData.ViewHolder> {

    private FeedData feed;
    private String title;
    private String subtitle;
    private Activity activity;

    public FeedItemData(FeedData feed, Activity activity) {
        super(R.layout.item_category);
        this.feed = feed;
        title = StringUtils.toPlainText(feed.getName());
        subtitle = StringUtils.toPlainText(feed.getBasicHomepage());
        this.activity = activity;
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public void bind(Context context, ViewHolder viewHolder) {
        viewHolder.itemView.setAlpha(0);
        viewHolder.itemView.animate().alpha(1).start();

        if (title != null)
            viewHolder.title.setText(title);
        if (subtitle != null)
            viewHolder.subtitle.setText(subtitle);

        if (viewHolder.recycler != null) {
            viewHolder.recycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

            List<ItemData> posts = new ArrayList<>();
            for (PostData post : feed.getPosts())
                posts.add(new PostItemData(post, activity));

            viewHolder.recycler.setAdapter(new ItemAdapter(posts));
        }
    }

    public static class ViewHolder extends ItemData.ViewHolder {

        private View background;
        private TextView title;
        private TextView subtitle;
        private RecyclerView recycler;

        public ViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.title);
            subtitle = v.findViewById(R.id.subtitle);
            recycler = v.findViewById(R.id.recycler);
        }
    }

}
