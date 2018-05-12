package com.crinoidtechnologies.general.template.recyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by nice on 6/8/2016.
 */
public abstract class BaseRecyclerViewHolder<T> extends RecyclerView.ViewHolder {

    protected View view;
    protected T data;

    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        initViews(itemView);
    }

    protected abstract void initViews(View view);

    public abstract void updateView(T data);
}

