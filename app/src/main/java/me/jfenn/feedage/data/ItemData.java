package me.jfenn.feedage.data;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class ItemData<T extends ItemData.ViewHolder> {

    private int layoutRes;

    public ItemData(@LayoutRes int layoutRes) {
        this.layoutRes = layoutRes;
    }

    public final int getLayoutRes() {
        return layoutRes;
    }

    public abstract T getViewHolder(View v);

    public abstract void bind(Context context, T viewHolder);

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View v) {
            super(v);
        }

    }

}
