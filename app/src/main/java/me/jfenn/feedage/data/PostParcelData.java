package me.jfenn.feedage.data;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import me.jfenn.feedage.lib.data.AtomFeedData;
import me.jfenn.feedage.lib.data.AuthorData;
import me.jfenn.feedage.lib.data.FeedData;
import me.jfenn.feedage.lib.data.PostData;

public class PostParcelData implements Parcelable {

    private PostData post;

    public PostParcelData(PostData post) {
        this.post = post;
    }

    protected PostParcelData(Parcel in) {
        FeedData feed = new AtomFeedData(in.readString(), in.readInt(), in.readInt());
        post = new PostData(feed);
        post.setTitle(in.readString());
        post.setDescription(in.readString());
        post.setContent(in.readString());
        post.setImageUrl(in.readString());
        post.setSourceUrl(in.readString());
        post.setPublishDate(in.readString());
        post.setUpdateDate(in.readString());

        int tags = in.readInt();
        for (int i = 0; i < tags; i++)
            post.addTag(in.readString());

        int authors = in.readInt();
        for (int i = 0; i < authors; i++) {
            AuthorData author = new AuthorData(in.readString());
            author.setHomepage(in.readString());
            author.setImageUrl(in.readString());
            post.addAuthor(author);
        }
    }

    public PostParcelData(SharedPreferences prefs, String name) {
        FeedData feed = new AtomFeedData(
                prefs.getString(name + "-parent-url", "https://example.com/"),
                prefs.getInt(name + "-parent-background", Color.WHITE),
                prefs.getInt(name + "-parent-text", Color.BLACK)
        );

        post = new PostData(feed);
        post.setTitle(prefs.getString(name + "-title", null));
        post.setDescription(prefs.getString(name + "-description", null));
        post.setContent(prefs.getString(name + "-content", null));
        post.setImageUrl(prefs.getString(name + "-image", null));
        post.setSourceUrl(prefs.getString(name + "-source", null));
        post.setPublishDate(prefs.getString(name + "-date-published", null));
        post.setUpdateDate(prefs.getString(name + "-date-updated", null));

        int tags = prefs.getInt(name + "-tags-length", 0);
        for (int i = 0; i < tags; i++)
            post.addTag(prefs.getString(name + "-tags-" + i, null));

        int authors = prefs.getInt(name + "-authors-length", 0);
        for (int i = 0; i < authors; i++) {
            AuthorData author = new AuthorData(prefs.getString(name + "-authors-" + i + "-name", null));
            author.setHomepage(prefs.getString(name + "-authors-" + i + "-homepage", null));
            author.setImageUrl(prefs.getString(name + "-authors-" + i + "-image", null));
            post.addAuthor(author);
        }
    }

    public SharedPreferences.Editor putPreference(SharedPreferences.Editor editor, String name) {
        editor = editor.putString(name + "-parent-url", post.getParent().getUrl())
                .putInt(name + "-parent-background", post.getParent().getBackgroundColor())
                .putInt(name + "-parent-text", post.getParent().getTextColor())
                .putString(name + "-title", post.getTitle())
                .putString(name + "-description", post.getDescription())
                .putString(name + "-content", post.getContent())
                .putString(name + "-image", post.getImageUrl())
                .putString(name + "-source", post.getSourceUrl())
                .putString(name + "-date-published", post.getPublishDateString())
                .putString(name + "-date-updated", post.getUpdateDateString())
                .putInt(name + "-tags-length", post.getTags().size())
                .putInt(name + "-authors-length", post.getAuthors().size());

        for (int i = 0; i < post.getTags().size(); i++)
            editor = editor.putString(name + "-tags-" + i, post.getTags().get(i));

        for (int i = 0; i < post.getAuthors().size(); i++) {
            AuthorData author = post.getAuthors().get(i);
            editor = editor.putString(name + "-authors-" + i + "-name", author.getName())
                    .putString(name + "-authors-" + i + "-homepage", author.getHomepage())
                    .putString(name + "-authors-" + i + "-image", author.getImageUrl());
        }

        return editor;
    }

    public PostData getPost() {
        return post;
    }

    public static final Creator<PostParcelData> CREATOR = new Creator<PostParcelData>() {
        @Override
        public PostParcelData createFromParcel(Parcel in) {
            return new PostParcelData(in);
        }

        @Override
        public PostParcelData[] newArray(int size) {
            return new PostParcelData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(post.getParent().getUrl());
        dest.writeInt(post.getParent().getBackgroundColor());
        dest.writeInt(post.getParent().getTextColor());
        dest.writeString(post.getTitle());
        dest.writeString(post.getDescription());
        dest.writeString(post.getContent());
        dest.writeString(post.getImageUrl());
        dest.writeString(post.getSourceUrl());
        dest.writeString(post.getPublishDateString());
        dest.writeString(post.getUpdateDateString());
        dest.writeInt(post.getTags().size());
        for (String tag : post.getTags())
            dest.writeString(tag);
        dest.writeInt(post.getAuthors().size());
        for (AuthorData author : post.getAuthors()) {
            dest.writeString(author.getName());
            dest.writeString(author.getHomepage());
            dest.writeString(author.getImageUrl());
        }
    }
}
