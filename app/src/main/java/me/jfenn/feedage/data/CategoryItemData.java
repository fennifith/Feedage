package me.jfenn.feedage.data;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.jfenn.feedage.R;
import me.jfenn.feedage.adapters.ItemAdapter;
import me.jfenn.feedage.lib.data.CategoryData;
import me.jfenn.feedage.lib.data.PostData;
import me.jfenn.feedage.utils.StringUtils;

public class CategoryItemData extends ItemData<CategoryItemData.ViewHolder> {

    private CategoryData category;

    public CategoryItemData(CategoryData category) {
        super(R.layout.item_category);
        this.category = category;
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public void bind(Context context, ViewHolder viewHolder) {
        if (category.getTitle() != null)
            viewHolder.title.setText(StringUtils.toPlainText(category.getTitle()));
        if (category.getDescription().length() > 0) {
            viewHolder.subtitle.setVisibility(View.VISIBLE);
            viewHolder.subtitle.setText(category.getDescription());
        } else viewHolder.subtitle.setVisibility(View.GONE);

        if (viewHolder.recycler != null) {
            viewHolder.recycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

            List<ItemData> posts = new ArrayList<>();
            for (PostData post : category.getPosts())
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
