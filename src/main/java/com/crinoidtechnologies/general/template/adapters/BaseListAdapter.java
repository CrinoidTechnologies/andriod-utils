package com.crinoidtechnologies.general.template.adapters;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.crinoidtechnologies.general.template.recyclerView.BaseClickableRecyclerViewHolder;
import com.crinoidtechnologies.general.template.recyclerView.BaseItemsClickableRecyclerAdapter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ${Vivek} on 4/30/2016 for Avante.Be careful
 */
public abstract class BaseListAdapter<T, VH extends BaseClickableRecyclerViewHolder<T>> extends BaseItemsClickableRecyclerAdapter<T, VH> {

    public static final String BUNDLE_INDEX_KEY = "index";
    public static final String BUNDLE_OBJ_KEY = "obj";
    public static final String BUNDLE_IS_SELECTED = "isSelected";

    protected BaseListAdapterListener listener;
    protected boolean hasMoreItems = true;

    public BaseListAdapter(List<T> items, BaseListAdapterListener listener) {
        super(items);
        this.listener = listener;
    }

    public static Bundle getBundleWithItemIndex(int index) {
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_INDEX_KEY, index);
        return bundle;
    }

    protected Bundle getBundleForController(int index) {
        Bundle bundle = new Bundle();

        if (index < 0) {
            Log.e("TAG", "getBundleForController: invalid index " + index);
            return bundle;
        }

        bundle.putInt(BUNDLE_INDEX_KEY, index);

        T item = items.get(index);

        if (item instanceof Serializable) {
            bundle.putSerializable(BUNDLE_OBJ_KEY, (Serializable) item);
        }

        return bundle;
    }

    @Override
    public boolean recyclerViewListClicked(RecyclerView.ViewHolder v, int position) {
        if (listener != null) {
            Bundle bundle = getBundleForController(position);
            listener.onListItemInteraction(bundle);
        }
        return true;
    }

    @Override
    public void onViewAttachedToWindow(VH holder) {
        super.onViewAttachedToWindow(holder);
        holder.onAttachToRecyclerView();
    }

    @Override
    public void onViewDetachedFromWindow(VH holder) {
        super.onViewDetachedFromWindow(holder);
        holder.onDetachToRecyclerView();
    }

    public boolean isHasMoreItems() {
        return hasMoreItems;
    }

    public void setHasMoreItems(boolean hasMoreItems) {
        this.hasMoreItems = hasMoreItems;
    }

    public BaseListAdapterListener getListener() {
        return listener;
    }

    public void setListener(BaseListAdapterListener listener) {
        this.listener = listener;
    }

    public interface BaseListAdapterListener {
        void onListItemInteraction(Bundle info);

        boolean fetchMoreItemsIfAvailable();
    }
}
