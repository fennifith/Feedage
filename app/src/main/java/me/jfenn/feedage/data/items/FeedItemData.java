package me.jfenn.feedage.data.items;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.jfenn.feedage.R;
import me.jfenn.feedage.activities.PostsActivity;
import me.jfenn.feedage.adapters.ItemAdapter;
import me.jfenn.feedage.lib.data.FeedData;
import me.jfenn.feedage.lib.data.PostData;
import me.jfenn.feedage.utils.StringUtils;

public class FeedItemData extends ItemData<FeedItemData.ViewHolder> {

    private FeedData feed;
    private String title;
    private String subtitle;

    private ItemAdapter adapter;
    private RecyclerView.RecycledViewPool viewPool;

    public FeedItemData(FeedData feed, RecyclerView.RecycledViewPool viewPool) {
        super(R.layout.item_category);
        this.feed = feed;
        this.viewPool = viewPool;

        title = StringUtils.toPlainText(feed.getName());
        subtitle = StringUtils.toPlainText(feed.getBasicHomepage());

        List<ItemData> items = new ArrayList<>();
        List<PostData> posts = feed.getPosts();
        for (int i = 0; i < posts.size() && i < 3; i++)
            items.add(new PostItemData(posts.get(i)));

        adapter = new ItemAdapter(items);
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v, viewPool);
    }

    @Override
    public void bind(Context context, ViewHolder viewHolder) {
        viewHolder.itemView.setAlpha(0);
        viewHolder.itemView.animate().alpha(1).start();

        if (title != null && title.length() > 0) {
            viewHolder.title.setText(title);
            viewHolder.subtitle.setText(subtitle);
            viewHolder.subtitle.setVisibility(View.VISIBLE);
        } else {
            viewHolder.title.setText(subtitle);
            viewHolder.subtitle.setVisibility(View.GONE);
        }

        if (adapter.getItemCount() > 0) {
            viewHolder.recycler.setVisibility(View.VISIBLE);
            viewHolder.recycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            viewHolder.recycler.setAdapter(adapter);

            viewHolder.viewMore.setVisibility(View.VISIBLE);
            viewHolder.viewMore.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), PostsActivity.class);
                intent.putExtra(PostsActivity.EXTRA_FEED, feed.getUrl());
                v.getContext().startActivity(intent);
            });
        } else {
            viewHolder.recycler.setVisibility(View.GONE);
            viewHolder.viewMore.setVisibility(View.GONE);
        }
    }

    public static class ViewHolder extends ItemData.ViewHolder {

        private TextView title;
        private TextView subtitle;
        private RecyclerView recycler;
        private View viewMore;

        public ViewHolder(View v, RecyclerView.RecycledViewPool viewPool) {
            super(v);
            title = v.findViewById(R.id.title);
            subtitle = v.findViewById(R.id.subtitle);
            recycler = v.findViewById(R.id.recycler);
            recycler.setRecycledViewPool(viewPool);
            viewMore = v.findViewById(R.id.viewMore);
        }
    }

}
