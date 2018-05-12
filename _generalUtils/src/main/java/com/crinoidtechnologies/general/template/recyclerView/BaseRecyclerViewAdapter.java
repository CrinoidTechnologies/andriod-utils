package com.crinoidtechnologies.general.template.recyclerView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by nice on 6/8/2016.
 */
public abstract class BaseRecyclerViewAdapter<T, VH extends BaseRecyclerViewHolder<T>> extends RecyclerView.Adapter<VH> {

    protected List<T> items;

    public BaseRecyclerViewAdapter(List<T> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(getRowLayoutId(viewType), parent, false);
        return getViewHolder(v, viewType);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.updateView(items.get(position));
    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    protected abstract VH getViewHolder(View v, int viewType);

    protected abstract int getRowLayoutId(int viewType);

    public void addItem(T item) {
        int previousCount = getItemCount();
        items.add(item);
        int newCount = getItemCount();
        if (newCount > previousCount) {
            if (items.size() == 0) {
                notifyDataSetChanged();
            } else {
                notifyItemInserted(items.size() - 1);
            }
        } else if (newCount == previousCount) {
//            notifyItemChanged(items.size() - 1);
            notifyDataSetChanged();
        } else {
            notifyDataSetChanged();
        }
    }

    public void removeItem(int i) {
        int previousCount = getItemCount();
        items.remove(i);
        int newCount = getItemCount();
        if (newCount < previousCount) {
            notifyItemRemoved(i);
        } else {
            notifyDataSetChanged();
        }
    }

    public void updateList(List<T> items) {
        if (items != null) {
            this.items = null;
            this.items = items;
            notifyDataSetChanged();
        }
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
