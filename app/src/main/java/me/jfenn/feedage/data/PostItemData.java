package me.jfenn.feedage.data;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import me.jfenn.feedage.R;
import me.jfenn.feedage.lib.data.PostData;

public class PostItemData extends ItemData<PostItemData.ViewHolder> {

    private PostData post;

    public PostItemData(PostData post) {
        super(R.layout.item_post);
        this.post = post;
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public void bind(Context context, ViewHolder viewHolder) {
        viewHolder.image.setVisibility(View.GONE);
        viewHolder.title.setText(post.getTitle());
        viewHolder.subtitle.setText(post.getDescription());
    }

    public static class ViewHolder extends ItemData.ViewHolder {

        private ImageView image;
        private TextView title;
        private TextView subtitle;

        public ViewHolder(View v) {
            super(v);
        }
    }

}
