package me.jfenn.feedage.data;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import me.jfenn.feedage.lib.data.AtomFeedData;
import me.jfenn.feedage.lib.data.FeedData;

public class FeedParcelData implements Parcelable {

    private FeedData feed;

    public FeedParcelData(FeedData feed) {
        this.feed = feed;
    }

    public FeedParcelData(SharedPreferences prefs, String name) {
        feed = new AtomFeedData(
                prefs.getString(name + "-url", null),
                prefs.getInt(name + "-backgroundcolor", Color.WHITE),
                prefs.getInt(name + "-textcolor", Color.BLACK)
        );
    }

    protected FeedParcelData(Parcel in) {
        feed = new AtomFeedData(in.readString(), in.readInt(), in.readInt());
    }

    public SharedPreferences.Editor putPreference(SharedPreferences.Editor editor, String name) {
        return editor.putString(name + "-url", feed.getUrl())
                .putInt(name + "-backgroundcolor", feed.getBackgroundColor())
                .putInt(name + "-textcolor", feed.getTextColor());
    }

    public FeedData getFeed() {
        return feed;
    }

    public static final Creator<FeedParcelData> CREATOR = new Creator<FeedParcelData>() {
        @Override
        public FeedParcelData createFromParcel(Parcel in) {
            return new FeedParcelData(in);
        }

        @Override
        public FeedParcelData[] newArray(int size) {
            return new FeedParcelData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(feed.getUrl());
        dest.writeInt(feed.getBackgroundColor());
        dest.writeInt(feed.getTextColor());
    }
}
