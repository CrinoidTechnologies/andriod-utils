package com.crinoidtechnologies.general.template.recyclerView;

import android.os.Bundle;

import java.util.List;

/**
 * Created by parkash on 6/15/2016.
 */
public abstract class BaseItemsClickableRecyclerAdapter<T, VH extends BaseClickableRecyclerViewHolder<T>> extends BaseRecyclerViewAdapter<T, VH> implements OnRecyclerViewHolderClickListener {
    public BaseItemsClickableRecyclerAdapter(List<T> items) {
        super(items);
    }

    @Override
    public void onImportantEvent(int type, int position, Bundle info) {

    }
}
