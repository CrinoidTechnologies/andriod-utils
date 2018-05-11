package com.crinoidtechnologies.general.template.recyclerView;

import android.view.View;

/**
 * Created by parkash on 6/15/2016.
 */
public abstract class BaseClickableRecyclerViewHolder<T> extends BaseRecyclerViewHolder<T> implements View.OnClickListener, View.OnLongClickListener {

    public OnRecyclerViewHolderClickListener listener;

    public BaseClickableRecyclerViewHolder(View itemView, OnRecyclerViewHolderClickListener listener) {
        super(itemView);
        this.listener = listener;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.recyclerViewListClicked(this, this.getAdapterPosition());
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (listener != null) {
            return listener.recyclerViewListItemLongClicked(this, this.getAdapterPosition());
        }
        return false;
    }

    public void onAttachToRecyclerView() {
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void onDetachToRecyclerView() {
        itemView.setOnClickListener(null);
        itemView.setOnLongClickListener(null);
    }

}
