package me.jfenn.feedage.data.items;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import me.jfenn.feedage.R;
import me.jfenn.feedage.activities.PostActivity;
import me.jfenn.feedage.data.PostParcelData;
import me.jfenn.feedage.lib.data.FeedData;
import me.jfenn.feedage.lib.data.PostData;
import me.jfenn.feedage.utils.StringUtils;

public class PostItemData extends ItemData<PostItemData.ViewHolder> {

    private PostData post;
    private String title;
    private String subtitle;
    private String imageUrl;

    public PostItemData(PostData post) {
        super(R.layout.item_post);
        this.post = post;
        title = StringUtils.toPlainText(post.getTitle());
        subtitle = StringUtils.toPlainText(post.getDescriptionText());
        imageUrl = post.getImageUrl();
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public void bind(Context context, ViewHolder viewHolder) {
        FeedData parent = post.getParent();

        if (title != null)
            viewHolder.title.setText(title);
        if (subtitle != null)
            viewHolder.subtitle.setText(subtitle);
        if (imageUrl != null) {
            viewHolder.image.setVisibility(View.VISIBLE);
            Glide.with(context).load(imageUrl).into(viewHolder.image);
        } else viewHolder.image.setVisibility(View.GONE);
        viewHolder.website.setText(parent.getBasicHomepage());

        int backgroundColor = parent.getBackgroundColor(),
                textColor = parent.getTextColor(),
                secondaryTextColor = Color.argb(150, Color.red(textColor), Color.green(textColor), Color.blue(textColor));

        viewHolder.background.setBackgroundColor(backgroundColor);
        viewHolder.title.setTextColor(textColor);
        viewHolder.subtitle.setTextColor(secondaryTextColor);
        viewHolder.website.setTextColor(secondaryTextColor);

        viewHolder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), PostActivity.class);
            intent.putExtra(PostActivity.EXTRA_POST_PARCEL, new PostParcelData(post));
            v.getContext().startActivity(intent);
        });
    }

    public static class ViewHolder extends ItemData.ViewHolder {

        private ImageView image;
        private TextView title;
        private TextView subtitle;
        private TextView website;
        private View background;

        public ViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.image);
            title = v.findViewById(R.id.title);
            subtitle = v.findViewById(R.id.subtitle);
            website = v.findViewById(R.id.website);
            background = v.findViewById(R.id.background);
        }
    }

}
