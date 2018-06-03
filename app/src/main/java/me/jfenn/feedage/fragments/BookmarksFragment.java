package me.jfenn.feedage.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.jfenn.feedage.Feedage;
import me.jfenn.feedage.R;
import me.jfenn.feedage.adapters.ItemAdapter;
import me.jfenn.feedage.data.items.ItemData;
import me.jfenn.feedage.data.items.PostItemData;
import me.jfenn.feedage.lib.data.PostData;

public class BookmarksFragment extends BasePagerFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        RecyclerView recycler = v.findViewById(R.id.recycler);

        List<ItemData> items = new ArrayList<>();
        for (PostData post : ((Feedage) getContext().getApplicationContext()).getBookmarks())
            items.add(new PostItemData(post, R.layout.item_post_horiz));

        recycler.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recycler.setAdapter(new ItemAdapter(items));

        return v;
    }

    @Override
    public String getTitle() {
        return "Bookmarks";
    }
}
