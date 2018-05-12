package com.crinoidtechnologies.general.template.recyclerView;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by changchen09 on 6/6/2016.
 */
public abstract class BaseSelectableRecyclerAdapter<T, VH extends BaseSelectableRecyclerViewHolder<T>> extends BaseItemsClickableRecyclerAdapter<T, VH> implements OnRecyclerViewHolderClickListener {
    @SuppressWarnings("unused")
    private static final String TAG = BaseSelectableRecyclerAdapter.class.getSimpleName();

    private SparseBooleanArray selectedItems;

    public BaseSelectableRecyclerAdapter(List<T> items) {
        super(items);
        selectedItems = new SparseBooleanArray();
    }

    public BaseSelectableRecyclerAdapter(List<T> items, List<Integer> selectedPositions) {
        super(items);
        selectedItems = new SparseBooleanArray();
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
            selectedItems.delete(position);
            //  viewHolder.onDeselect();
        } else {
            selectedItems.put(position, true);
            //  viewHolder.onSelect();
        }
        notifyItemChanged(position);

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
    public boolean recyclerViewListClicked(RecyclerView.ViewHolder v, int position) {
        if (selectedItems.size() != 0) {
            toggleSelection((BaseSelectableRecyclerViewHolder) v, position);
            return true;
        }
        return false;
    }

    @Override
    public boolean recyclerViewListItemLongClicked(RecyclerView.ViewHolder v, int position) {
        // if (selectedItems.size()==0){
        toggleSelection((BaseSelectableRecyclerViewHolder) v, position);
        // }
        return true;
    }
}