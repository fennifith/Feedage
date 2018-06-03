package me.jfenn.feedage.data.items;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import me.jfenn.feedage.R;
import me.jfenn.feedage.activities.PostsActivity;
import me.jfenn.feedage.lib.data.AuthorData;

public class AuthorItemData extends ItemData<AuthorItemData.ViewHolder> {

    private AuthorData author;
    private String feedUrl;

    public AuthorItemData(AuthorData author, String feedUrl) {
        super(R.layout.item_author);
        this.author = author;
        this.feedUrl = feedUrl;
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public void bind(Context context, ViewHolder viewHolder) {
        viewHolder.name.setText(author.getName());

        if (author.getImageUrl() != null)
            Glide.with(context).load(author.getImageUrl()).into(viewHolder.image);
        else viewHolder.image.setVisibility(View.GONE);

        if (author.getHomepage() != null)
            viewHolder.itemView.setOnClickListener(v -> v.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(author.getHomepage()))));
        else viewHolder.itemView.setOnClickListener(null);

        viewHolder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), PostsActivity.class);
            intent.putExtra(PostsActivity.EXTRA_AUTHOR, author.getName());
            intent.putExtra(PostsActivity.EXTRA_FEED, feedUrl);
            v.getContext().startActivity(intent);
        });
    }

    public static class ViewHolder extends ItemData.ViewHolder {

        private ImageView image;
        private TextView name;

        public ViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.image);
            name = v.findViewById(R.id.name);
        }
    }

}
