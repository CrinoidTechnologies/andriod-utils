package com.crinoidtechnologies.general.template.recyclerView;

import android.view.View;

/**
 * Created by parkash on 6/15/2016.
 */
public abstract class BaseSelectableRecyclerViewHolder<T> extends BaseClickableRecyclerViewHolder<T> {

    protected boolean isSelected = false;

    public BaseSelectableRecyclerViewHolder(View itemView, OnRecyclerViewHolderClickListener listerner) {
        super(itemView, listerner);
    }

    void onSelect() {
        isSelected = true;
    }

    void onDeselect() {
        isSelected = false;
    }

}
