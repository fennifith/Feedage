package me.jfenn.feedage.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.jfenn.feedage.R;
import me.jfenn.feedage.adapters.ItemAdapter;
import me.jfenn.feedage.data.items.CategoryItemData;
import me.jfenn.feedage.data.items.ItemData;
import me.jfenn.feedage.lib.FeedageLib;
import me.jfenn.feedage.lib.data.CategoryData;
import me.jfenn.feedage.lib.data.FeedData;

public class CategoriesFragment extends BasePagerFragment implements FeedageLib.OnCategoriesUpdatedListener {

    private RecyclerView recycler;
    private View refresh;
    private boolean shouldSwap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recycler, container, false);
        recycler = v.findViewById(R.id.recycler);
        refresh = v.findViewById(R.id.refresh);

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        onCategoriesUpdated(getFeedage().getCategories());
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFeedage().addListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getFeedage().removeListener(this);
    }

    @Override
    public void onFeedsUpdated(List<FeedData> feeds) {

    }

    @Override
    public void onCategoriesUpdated(List<CategoryData> categories) {
        List<ItemData> items = new ArrayList<>();
        for (CategoryData category : categories)
            items.add(new CategoryItemData(category));

        if (recycler.getAdapter() != null && recycler.getAdapter().getItemCount() > 0) {
            if (shouldSwap) {
                shouldSwap = false;
                recycler.swapAdapter(new ItemAdapter(items), true);
                refresh.setOnClickListener(null);
                refresh.animate().alpha(0).start();
            } else {
                refresh.setOnClickListener(v -> {
                    shouldSwap = true;
                    onCategoriesUpdated(categories);
                });
                refresh.animate().alpha(1).start();
            }
        } else recycler.setAdapter(new ItemAdapter(items));
    }

    @Override
    public String getTitle() {
        return "Categories";
    }
}
