package com.crinoidtechnologies.general.template.recyclerView;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;

import com.crinoidtechnologies.general.template.adapters.BaseListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${Vivek} on 4/30/2016 for Avante.Be careful
 */
public abstract class BaseSelectableListAdapter<T, VH extends BaseSelectableRecyclerViewHolder<T>> extends BaseListAdapter<T, VH> {
    protected boolean isSingleSelection = false;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    public BaseSelectableListAdapter( List<T> items, BaseListAdapterListener listener, List<Integer> selectedPositions) {
        super(items, listener);
        if (selectedPositions != null) {
            for (int i = 0; i < selectedPositions.size(); i++) {
                selectedItems.put(selectedPositions.get(i), true);
            }
        }
    }

    /**
     * Indicates if the item at position is selected
     *
     * @param position Position of the item to check
     * @return true if the item is selected, false otherwise
     */
    public boolean isSelected(int position) {
        return getSelectedItems().contains(position);
    }

    /**
     * Toggle the selection status of the item at a given position
     *
     * @param position Position of the item to toggle the selection status for
     */
    public void toggleSelection(BaseSelectableRecyclerViewHolder viewHolder, int position) {

        if (selectedItems.get(position, false)) {
            if (!isSingleSelection) {
                selectedItems.delete(position);
            }
        } else {
            if (isSingleSelection) {
                clearSelection();
            }
            selectedItems.put(position, true);
        }
        notifyItemChanged(position);

    }

    protected Bundle getBundleForController(int index) {
        Bundle bundle = super.getBundleForController(index);
        bundle.putBoolean(BUNDLE_IS_SELECTED, isSelected(index));
        return bundle;
    }

    @Override
    public boolean recyclerViewListClicked(RecyclerView.ViewHolder v, int position) {
        if (isSingleSelection) {
            recyclerViewListItemLongClicked(v, position);
        } else {
            if (selectedItems.size() != 0) {
                toggleSelection((BaseSelectableRecyclerViewHolder) v, position);
            }
        }
        return super.recyclerViewListClicked(v, position);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if (selectedItems.get(position, false)) {
            holder.onSelect();
        } else {
            holder.onDeselect();
        }
        super.onBindViewHolder(holder, position);
    }

    /**
     * Clear the selection status for all items
     */
    public void clearSelection() {
        List<Integer> selection = getSelectedItems();
        selectedItems.clear();
        for (Integer i : selection) {
            notifyItemChanged(i);
        }
    }

    public void nothingClearSelection() {
        selectedItems.clear();
    }

    /**
     * Count the selected items
     *
     * @return Selected items count
     */
    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    /**
     * Indicates the list of selected items
     *
     * @return List of selected items ids
     */
    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    @Override
    public boolean recyclerViewListItemLongClicked(RecyclerView.ViewHolder v, int position) {
        toggleSelection((BaseSelectableRecyclerViewHolder) v, position);
        return true;
    }
}
