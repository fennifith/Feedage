package me.jfenn.feedage.data.items;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.jfenn.feedage.R;
import me.jfenn.feedage.activities.PostsActivity;
import me.jfenn.feedage.adapters.ItemAdapter;
import me.jfenn.feedage.lib.data.CategoryData;
import me.jfenn.feedage.lib.data.PostData;
import me.jfenn.feedage.utils.StringUtils;

public class CategoryItemData extends ItemData<CategoryItemData.ViewHolder> {

    private CategoryData category;
    private String title;
    private String subtitle;

    private ItemAdapter adapter;
    private RecyclerView.RecycledViewPool viewPool;

    public CategoryItemData(CategoryData category, RecyclerView.RecycledViewPool viewPool) {
        super(R.layout.item_category);
        this.category = category;
        this.viewPool = viewPool;

        title = StringUtils.toPlainText(category.getTitle());
        if (category.getDescription().length() > 0)
            subtitle = category.getDescriptionSentence();

        List<PostItemData> items = new ArrayList<>();
        List<PostData> posts = category.getPosts();
        for (int i = 0; i < posts.size() && i < 3; i++)
            items.add(new PostItemData(posts.get(i)));

        Collections.sort(items);
        adapter = new ItemAdapter(new ArrayList<>(items));
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v, viewPool);
    }

    @Override
    public void bind(Context context, ViewHolder viewHolder) {
        viewHolder.itemView.setAlpha(0);
        viewHolder.itemView.animate().alpha(1).start();

        if (title != null)
            viewHolder.title.setText(title);
        if (subtitle != null && subtitle.length() > 0) {
            viewHolder.subtitle.setVisibility(View.VISIBLE);
            viewHolder.subtitle.setText(subtitle);
        } else viewHolder.subtitle.setVisibility(View.GONE);

        if (viewHolder.recycler != null) {
            viewHolder.recycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            viewHolder.recycler.setAdapter(adapter);
        }

        viewHolder.viewMore.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), PostsActivity.class);
            intent.putExtra(PostsActivity.EXTRA_CATEGORY, category.getTitle());
            v.getContext().startActivity(intent);
        });
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
