package me.jfenn.feedage.data;

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
