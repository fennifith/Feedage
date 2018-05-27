package me.jfenn.feedage.data.items;

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

    public FeedItemData(FeedData feed) {
        super(R.layout.item_category);
        this.feed = feed;
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public void bind(Context context, ViewHolder viewHolder) {
        if (feed.getName() != null)
            viewHolder.title.setText(StringUtils.toPlainText(feed.getName()));
        if (feed.getHomepage() != null)
            viewHolder.subtitle.setText(StringUtils.toPlainText(feed.getBasicHomepage()));

        if (viewHolder.recycler != null) {
            viewHolder.recycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

            List<ItemData> posts = new ArrayList<>();
            for (PostData post : feed.getPosts())
                posts.add(new PostItemData(post));

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
