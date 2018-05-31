package me.jfenn.feedage.data;

import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import me.jfenn.feedage.lib.data.CategoryData;
import me.jfenn.feedage.lib.data.PostData;
import me.jfenn.feedage.lib.utils.SOAMCOS;

public class CategoryParcelData implements Parcelable {

    private CategoryData category;

    public CategoryParcelData(CategoryData category) {
        this.category = category;
    }

    protected CategoryParcelData(Parcel in) {
        category = new CategoryData();
        int posts = in.readInt();
        for (int i = 0; i < posts; i++)
            category.addPost(new PostParcelData(in).getPost());

        int averages = in.readInt();
        for (int i = 0; i < averages; i++)
            category.addAverage(new SOAMCOS.WordAverage(in.readString(), in.readString()));
    }

    public CategoryParcelData(SharedPreferences prefs, String name) {
        category = new CategoryData();
        int posts = prefs.getInt(name + "-posts-length", 0);
        for (int i = 0; i < posts; i++)
            category.addPost(new PostParcelData(prefs, name + "-posts-" + i).getPost());

        int averages = prefs.getInt(name + "-averages-length", 0);
        for (int i = 0; i < averages; i++) {
            category.addAverage(new SOAMCOS.WordAverage(
                    prefs.getString(name + "-averages-" + i + "-first", null),
                    prefs.getString(name + "-averages-" + i + "-last", null)
            ));
        }
    }

    public SharedPreferences.Editor putPreference(SharedPreferences.Editor editor, String name) {
        editor = editor.putInt(name + "-posts-length", category.getPosts().size())
                .putInt(name + "-averages-length", category.getAverages().size());

        for (int i = 0; i < category.getPosts().size(); i++)
            editor = new PostParcelData(category.getPosts().get(i)).putPreference(editor, name + "--posts-" + i);

        for (int i = 0; i < category.getAverages().size(); i++) {
            SOAMCOS.WordAverage average = category.getAverages().get(i);
            editor = editor.putString(name + "-averages-" + i + "-first", average.getFirstWord())
                    .putString(name + "-averages-" + i + "-last", average.getLastWord());
        }

        return editor;
    }

    public CategoryData getCategory() {
        return category;
    }

    public static final Creator<CategoryParcelData> CREATOR = new Creator<CategoryParcelData>() {
        @Override
        public CategoryParcelData createFromParcel(Parcel in) {
            return new CategoryParcelData(in);
        }

        @Override
        public CategoryParcelData[] newArray(int size) {
            return new CategoryParcelData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(category.getPosts().size());
        for (PostData post : category.getPosts())
            new PostParcelData(post).writeToParcel(dest, flags);

        dest.writeInt(category.getAverages().size());
        for (SOAMCOS.WordAverage average : category.getAverages()) {
            dest.writeString(average.getFirstWord());
            dest.writeString(average.getLastWord());
        }
    }
}
