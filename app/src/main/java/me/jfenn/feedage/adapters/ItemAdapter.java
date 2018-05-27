package me.jfenn.feedage.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import me.jfenn.feedage.data.items.ItemData;

public class ItemAdapter extends RecyclerView.Adapter<ItemData.ViewHolder> {

    private List<ItemData> infos;

    public ItemAdapter(List<ItemData> infos) {
        this.infos = infos;
    }

    @NonNull
    @Override
    public ItemData.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemData item = infos.get(viewType);
        return item.getViewHolder(LayoutInflater.from(parent.getContext()).inflate(item.getLayoutRes(), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemData.ViewHolder holder, int position) {
        infos.get(position).bind(holder.itemView.getContext(), holder);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return infos.size();
    }

}
